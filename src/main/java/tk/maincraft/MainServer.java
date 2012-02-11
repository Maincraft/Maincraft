package tk.maincraft;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import me.main__.util.SerializationConfig.SerializationConfig;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.Recipe;
import org.bukkit.map.MapView;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.SimpleServicesManager;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.permissions.DefaultPermissions;

import tk.maincraft.command.ConfigCommand;
import tk.maincraft.command.MainConsoleCommandSender;
import tk.maincraft.entity.MainPlayer;
import tk.maincraft.network.NetworkController;
import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.out.LoginPacket;
import tk.maincraft.packet.out.OutputPacket;
import tk.maincraft.packet.out.PlayerListItemPacket;
import tk.maincraft.packet.out.PlayerPositionAndLookPacket;
import tk.maincraft.packet.out.SpawnPositionPacket;
import tk.maincraft.packet.out.impl.OutputLoginPacket;
import tk.maincraft.packet.out.impl.OutputPlayerListItemPacket;
import tk.maincraft.packet.out.impl.OutputPlayerPositionAndLookPacket;
import tk.maincraft.packet.out.impl.OutputSpawnPositionPacket;
import tk.maincraft.scheduler.MainScheduler;
import tk.maincraft.util.Versioning;
import tk.maincraft.util.config.MaincraftConfig;
import tk.maincraft.world.CleanroomChunkGenerator;
import tk.maincraft.world.EntityManager;
import tk.maincraft.world.MainWorld;

import com.avaje.ebean.config.ServerConfig;

public class MainServer implements Server {
    public static final int MINECRAFT_SERVER_PORT = 25566;
    public static final Random rand = new Random();

    private final NetworkController netController;
    private final Logger log;
    private MaincraftConfig config;

    private final ServicesManager servicesManager = new SimpleServicesManager();
    private final MainScheduler scheduler = new MainScheduler();
    private final SimpleCommandMap commandMap = new SimpleCommandMap(this);
    private final PluginManager pluginManager = new SimplePluginManager(this, commandMap);
    private final ConsoleCommandSender commandSender;
    private final MaincraftConsoleThread consoleThread = new MaincraftConsoleThread(this);
    private final Thread mainThread = new MainServerThread();

    private final Map<String, MainWorld> worlds = new ConcurrentHashMap<String, MainWorld>();
    private final Set<MainPlayer> onlinePlayers = Collections
            .newSetFromMap(new ConcurrentHashMap<MainPlayer, Boolean>());

    private boolean stopping = false;

    public MainServer() {
        log = Logger.getLogger("Maincraft");
        log.setUseParentHandlers(false);
        Handler logHandler = new Handler() {
            public void publish(LogRecord record) {
                System.out.println(this.getFormatter().format(record));
            }

            public void flush() {
                System.out.flush();
            }

            public void close() throws SecurityException {
            }
        };
        logHandler.setFormatter(new Formatter() {
            private final DateFormat df = new SimpleDateFormat("HH:mm:ss");

            public String format(LogRecord record) {
                StringBuilder sb = new StringBuilder();

                sb.append("[").append(df.format(new Date())).append("]");
                sb.append(" ");
                sb.append("[").append(record.getLevel().getName()).append("]");
                sb.append(" ");
                sb.append(record.getMessage());

                return sb.toString();
            }
        });
        log.addHandler(logHandler);
        log.setLevel(Level.ALL);

        // register our commands
        commandMap.register("maincraft", new ConfigCommand(this));

        // we want to handle "noob"-plugins xD
        Logger.getLogger("Minecraft").setParent(log);

        netController = new NetworkController(MINECRAFT_SERVER_PORT, this);

        Bukkit.setServer(this);
        Maincraft.setServer(this);

        // this stuff uses Bukkit.getServer()
        commandSender = new MainConsoleCommandSender(this);

        loadPlugins();
        // TODO more...?

        loadConfig();

        // let's add one dummy-world:
        enablePlugins(PluginLoadOrder.STARTUP);
        worlds.put("world", new MainWorld(this, "world", Environment.NORMAL,
                new CleanroomChunkGenerator()));
        enablePlugins(PluginLoadOrder.POSTWORLD);
    }

    public FileConfiguration openConfig() {
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File("maincraft.yml"));
        return config;
    }

    public void saveConfig() {
        File configFile = new File("maincraft.yml");
        FileConfiguration fileConfig = null;
        try {
            configFile.createNewFile();
            fileConfig = YamlConfiguration.loadConfiguration(configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (fileConfig != null) {
            try {
                fileConfig.set("config", config);
                fileConfig.save(configFile);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void loadConfig() {
        // load config
        SerializationConfig.registerAll(MaincraftConfig.class); // register!
        File configFile = new File("maincraft.yml");
        FileConfiguration fileConfig = null;
        MaincraftConfig wantedConfig = null;
        try {
            configFile.createNewFile();
            fileConfig = YamlConfiguration.loadConfiguration(configFile);
            MaincraftConfig loadedConfig = (MaincraftConfig) fileConfig.get("config");
            wantedConfig = loadedConfig;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            config = ((wantedConfig == null) ? new MaincraftConfig() : wantedConfig);
        }
        // ... and save it
        if (fileConfig != null) {
            try {
                fileConfig.set("config", config);
                fileConfig.save(configFile);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public boolean setConfigProperty(String property, String value) {
        log.finest(String.format("setConfigProperty(%s,%s)", property, value));
        try {
            String[] nodes = property.split("\\.");
            log.finest("Nodes: " + nodes.length);
            // oh, reflection...
            Field theField = MaincraftConfig.class.getDeclaredField(nodes[0]);
            SerializationConfig parentObject = config;
            theField.setAccessible(true);
            SerializationConfig configObject = (SerializationConfig) theField.get(parentObject);
            theField.setAccessible(false);
            for (int i = 1; i < (nodes.length - 1); i++) {
                log.finest("Iterating through node: " + nodes[i]);
                if (theField.getType() != configObject.getClass())
                    throw new Exception("spinne oda bine ei im himme");
                theField = theField.getType().getDeclaredField(nodes[i]);
                parentObject = configObject;
                configObject = (SerializationConfig) theField.get(configObject);
            }
            // theField is now the field containing the config-object that contains our property
            // configObject is the config-object that contains our property
            Map<String, Object> serializedState = configObject.serialize();
            serializedState.put(nodes[nodes.length - 1], value);
            Constructor<? extends SerializationConfig> serializationCtor = configObject.getClass()
                    .getConstructor(Map.class);
            configObject = serializationCtor.newInstance(serializedState);
            theField.setAccessible(true);
            log.finest(String.format("parent: %s - config: %s", parentObject, configObject));
            theField.set(parentObject, configObject);
            theField.setAccessible(false);
            saveConfig();
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public MaincraftConfig getConfig() {
        return config;
    }

    public void loadPlugins() {
        pluginManager.registerInterface(JavaPluginLoader.class);

        File pluginFolder = new File("plugins");

        if (pluginFolder.exists()) {
            Plugin[] plugins = pluginManager.loadPlugins(pluginFolder);
            for (Plugin plugin : plugins) {
                try {
                    plugin.onLoad();
                } catch (Throwable ex) {
                    getLogger().log(
                            Level.SEVERE,
                            ex.getMessage() + " initializing "
                                    + plugin.getDescription().getFullName()
                                    + " (Is it up to date?)", ex);
                }
            }
        }
        else {
            pluginFolder.mkdir();
        }
    }

    public void enablePlugins(PluginLoadOrder type) {
        Plugin[] plugins = pluginManager.getPlugins();

        for (Plugin plugin : plugins) {
            if ((!plugin.isEnabled()) && (plugin.getDescription().getLoad() == type)) {
                loadPlugin(plugin);
            }
        }

        if (type == PluginLoadOrder.POSTWORLD) {
            commandMap.registerServerAliases();
            // loadCustomPermissions();
            DefaultPermissions.registerCorePermissions();
        }
    }

    private void loadPlugin(Plugin plugin) {
        try {
            pluginManager.enablePlugin(plugin);

            List<Permission> perms = plugin.getDescription().getPermissions();

            for (Permission perm : perms) {
                try {
                    pluginManager.addPermission(perm);
                } catch (IllegalArgumentException ex) {
                    getLogger().log(
                            Level.WARNING,
                            "Plugin " + plugin.getDescription().getFullName()
                                    + " tried to register permission '" + perm.getName()
                                    + "' but it's already registered", ex);
                }
            }
        } catch (Throwable ex) {
            getLogger().log(
                    Level.SEVERE,
                    ex.getMessage() + " loading " + plugin.getDescription().getFullName()
                            + " (Is it up to date?)", ex);
        }
    }

    public void start() {
        // setup listeners etc.
        try {
            netController.startListening();
        } catch (IOException e) {
            log.severe("FAILED TO START SERVER!");
            e.printStackTrace();
            shutdown();
            return;
        }
        mainThread.start();

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                if (!stopping) {
                    System.err.println("=========================================");
                    System.err.println("What idiot tried to kill the server!?");
                    System.err
                            .println("Fortunately, the programmer was smart and added this piece of code :P");
                    System.err
                            .println("I'm now trying to initiate a safe shutdown, but I can't guarantee that this works!");
                    try {
                        stopping = true;
                        mainThread.interrupt();
                        mainThread.join();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }));

        // start console-thread
        consoleThread.start();

        log.info("started");
    }

    public void tick(long tickCounter) {
        // TODO more...?
        scheduler.tick();
    }

    public void shutdown() {
        // notify the main-thread
        stopping = true;
        // wait for exit
        try {
            mainThread.join();
        } catch (InterruptedException e) {
        }
    }

    public boolean tryLoginPlayer(String playername, PacketClient client) {
        // let's first see what the plugins say:
        PlayerPreLoginEvent pple = EventFactory.onPlayerPreLogin(playername, client.getIp());
        if (pple.getResult() != PlayerPreLoginEvent.Result.ALLOWED) {
            client.kick(pple.getKickMessage());
            return true;
        }

        // username banned?
        if (getBannedUsernames().contains(playername)) {
            client.kick("You are banned from this server!");
            return true;
        }

        // ip banned?
        if (getIPBans().contains(client.getIpString())) {
            client.kick("Your IP address is banned from this server!");
            return true;
        }

        // Whitelist?
        if (hasWhitelist() && !getWhitelistedUsernames().contains(playername)) {
            client.kick("You are not whitelisted on this server!");
            return true;
        }

        // server full?
        if (getOnlinePlayers().length >= getMaxPlayers()) {
            client.kick("The server is full!");
            return true;
        }

        // already logged in?
        for (Player p : getOnlinePlayers()) {
            if (p.getName().equalsIgnoreCase(playername)) {
                client.kick("You are already logged in from another location!");
                return true;
            }
        }

        // yay, the player can join :D
        MainWorld world = (MainWorld) this.getWorld("world"); // TODO variable
        EntityManager entityManager = world.getEntityManager();
        MainPlayer playerObj = entityManager.newPlayer(playername, client);

        int entityId = playerObj.getEntityId();
        long seed = playerObj.getWorld().getSeed();
        int mode = this.getDefaultGameMode().getValue();
        byte dimension = 0; // TODO
        byte difficulty = (byte) playerObj.getWorld().getDifficulty().getValue();
        byte worldHeight = 0; // TODO
        byte maxPlayers = (byte) this.getMaxPlayers();
        LoginPacket loginPacket = new OutputLoginPacket(client, entityId, seed, mode, dimension,
                difficulty, worldHeight, maxPlayers);
        loginPacket.send();

        client.setPlayer(playerObj);
        entityManager.sendChunkUpdates(playerObj);

        // now do the spawnpoint-packet
        SpawnPositionPacket spawnPacket = new OutputSpawnPositionPacket(client,
                world.getSpawnLocation());
        spawnPacket.send();

        PlayerPositionAndLookPacket posAndLookPacket = new OutputPlayerPositionAndLookPacket(
                client, playerObj.getLocation(), 0, true);
        posAndLookPacket.send();

        playerObj.setOp(true); // FIXME: remove this, it's only debug!

        log.finest("logon packet sent");
        onlinePlayers.add(playerObj);

        PlayerJoinEvent event = new PlayerJoinEvent(playerObj, new StringBuilder(
                ChatColor.YELLOW.toString()).append(playername).append(" joined the game.")
                .toString());
        this.getPluginManager().callEvent(event);
        if ((event.getJoinMessage() != null) && (!event.getJoinMessage().isEmpty()))
            this.broadcastMessage(event.getJoinMessage());

        // write join-message to console
        log.info(String.format("Player '%s' connected from '%s'", playerObj.getName(),
                client.getIpString()));

        // add player to playerList
        // TODO ping
        PlayerListItemPacket playerListPacket = new OutputPlayerListItemPacket(null, playername,
                true, (short) 0);
        playerListPacket.send();

        // update that player's playerlist
        for (Player player : getOnlinePlayers()) {
            PlayerListItemPacket otherPlayerListPacket = new OutputPlayerListItemPacket(client,
                    player.getName(), true, (short) 0);
            otherPlayerListPacket.send();
        }

        return true;
    }

    public Set<String> getBannedUsernames() {
        // TODO Auto-generated method stub
        return new HashSet<String>();
    }

    public Set<String> getWhitelistedUsernames() {
        // TODO Auto-generated method stub
        return new HashSet<String>();
    }

    public String getMOTD() {
        // TODO Auto-generated method stub
        return "Example-MOTD";
    }

    public String getName() {
        return "Maincraft";
    }

    public String getVersion() {
        return "dev";
    }

    public String getBukkitVersion() {
        return Versioning.getBukkitVersion();
    }

    public Player[] getOnlinePlayers() {
        return onlinePlayers.toArray(new Player[onlinePlayers.size()]);
    }

    public int getMaxPlayers() {
        // TODO Auto-generated method stub
        return 2;
    }

    public int getPort() {
        // TODO Auto-generated method stub
        return MINECRAFT_SERVER_PORT;
    }

    public int getViewDistance() {
        return getConfig().getIOSettings().getViewDistance();
    }

    public String getIp() {
        return netController.getMyIPString();
    }

    public String getServerName() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getServerId() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean getAllowEnd() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean getAllowNether() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean hasWhitelist() {
        // TODO Auto-generated method stub
        return false;
    }

    public void setWhitelist(boolean value) {
        // TODO Auto-generated method stub

    }

    public Set<OfflinePlayer> getWhitelistedPlayers() {
        // TODO Auto-generated method stub
        return new HashSet<OfflinePlayer>();
    }

    public void reloadWhitelist() {
        // TODO Auto-generated method stub

    }

    public int broadcastMessage(String message) {
        return broadcast(message, BROADCAST_CHANNEL_USERS);
    }

    public String getUpdateFolder() {
        return "update";
    }

    public File getUpdateFolderFile() {
        return new File(getUpdateFolder());
    }

    public Player getPlayer(String name) {
        // TODO Auto-generated method stub
        return getPlayerExact(name);
    }

    public Player getPlayerExact(String name) {
        for (MainPlayer player : onlinePlayers) {
            if (player.getName().equals(name))
                return player;
        }
        return null;
    }

    @Override
    public List<Player> matchPlayer(String name) {
        // TODO Auto-generated method stub
        return new ArrayList<Player>();
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public BukkitScheduler getScheduler() {
        return scheduler;
    }

    public ServicesManager getServicesManager() {
        return servicesManager;
    }

    public List<World> getWorlds() {
        return Collections.unmodifiableList(new ArrayList<World>(worlds.values()));
    }

    public World createWorld(String name, World.Environment environment) {
        return WorldCreator.name(name).environment(environment).createWorld();
    }

    public World createWorld(String name, World.Environment environment, long seed) {
        return WorldCreator.name(name).environment(environment).seed(seed).createWorld();
    }

    public World createWorld(String name, Environment environment, ChunkGenerator generator) {
        return WorldCreator.name(name).environment(environment).generator(generator).createWorld();
    }

    public World createWorld(String name, Environment environment, long seed,
            ChunkGenerator generator) {
        return WorldCreator.name(name).environment(environment).seed(seed).generator(generator)
                .createWorld();
    }

    public World createWorld(WorldCreator creator) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean unloadWorld(String name, boolean save) {
        return unloadWorld(getWorld(name), save);
    }

    public boolean unloadWorld(World world, boolean save) {
        // TODO Auto-generated method stub
        return false;
    }

    public World getWorld(String name) {
        return worlds.get(name);
    }

    public World getWorld(UUID uid) {
        for (World w : worlds.values()) {
            if (w.getUID().equals(uid))
                return w;
        }
        return null;
    }

    public MapView getMap(short id) {
        // TODO Auto-generated method stub
        return null;
    }

    public MapView createMap(World world) {
        // TODO Auto-generated method stub
        return null;
    }

    public void reload() {
        // TODO Auto-generated method stub

    }

    public Logger getLogger() {
        return log;
    }

    public PluginCommand getPluginCommand(String name) {
        Command command = commandMap.getCommand(name);

        if (command instanceof PluginCommand) {
            return (PluginCommand) command;
        }
        else {
            return null;
        }
    }

    public void savePlayers() {
        // TODO Auto-generated method stub

    }

    public boolean dispatchCommand(CommandSender sender, String commandLine)
            throws CommandException {
        if (commandMap.dispatch(sender, commandLine)) {
            return true;
        }

        sender.sendMessage("Failed to execute your command :(");

        return false;
    }

    public void configureDbConfig(ServerConfig config) {
        // TODO Auto-generated method stub

    }

    public boolean addRecipe(Recipe recipe) {
        // TODO Auto-generated method stub
        return false;
    }

    public Map<String, String[]> getCommandAliases() {
        // TODO Auto-generated method stub
        return new HashMap<String, String[]>();
    }

    public int getSpawnRadius() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void setSpawnRadius(int value) {
        // TODO Auto-generated method stub

    }

    public boolean getOnlineMode() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean getAllowFlight() {
        // TODO Auto-generated method stub
        return true;
    }

    public int broadcast(String message, String permission) {
        int count = 0;
        for (Player p : this.getOnlinePlayers()) {
            if (p.hasPermission(permission)) {
                p.sendMessage(message);
                count++;
            }
        }
        return count;
    }

    public OfflinePlayer getOfflinePlayer(String name) {
        return new MainOfflinePlayer(name);
    }

    public Set<String> getIPBans() {
        // TODO Auto-generated method stub
        return new HashSet<String>();
    }

    public void banIP(String address) {
        // TODO Auto-generated method stub

    }

    public void unbanIP(String address) {
        // TODO Auto-generated method stub

    }

    public Set<OfflinePlayer> getBannedPlayers() {
        // TODO Auto-generated method stub
        return new HashSet<OfflinePlayer>();
    }

    public Set<OfflinePlayer> getOperators() {
        // TODO Auto-generated method stub
        return new HashSet<OfflinePlayer>();
    }

    public GameMode getDefaultGameMode() {
        // TODO Auto-generated method stub
        return GameMode.SURVIVAL;
    }

    public void setDefaultGameMode(GameMode mode) {
        // TODO Auto-generated method stub

    }

    public ConsoleCommandSender getConsoleSender() {
        return commandSender;
    }

    public File getWorldContainer() {
        // TODO Auto-generated method stub
        return null;
    }

    public OfflinePlayer[] getOfflinePlayers() {
        // TODO Auto-generated method stub
        return new OfflinePlayer[0];
    }

    private class MainServerThread extends Thread {
        public static final long MILLISECONDS_PER_TICK = 50L;

        private long lastTickTime;
        private long tickCounter = 0;
        private Object lock = new Object();

        public void run() {
            synchronized (lock) {
                lastTickTime = System.currentTimeMillis();
                while (!stopping) {
                    tick(tickCounter);

                    long thisTickTime = System.currentTimeMillis();
                    long timeSinceLastTick = thisTickTime - lastTickTime;
                    if (timeSinceLastTick < MILLISECONDS_PER_TICK) {
                        try {
                            Thread.yield();
                            lock.wait(MILLISECONDS_PER_TICK - timeSinceLastTick);
                        } catch (InterruptedException e) {
                            if (stopping) {
                                netController.stopListeningNOW();
                                consoleThread.shutdown();
                                System.exit(0);
                            }
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    else if (timeSinceLastTick > MILLISECONDS_PER_TICK) {
                        getLogger().info(
                                String.format("You're WAY too slow (%d/%d) ...", timeSinceLastTick,
                                        MILLISECONDS_PER_TICK));
                    }

                    lastTickTime = System.currentTimeMillis();
                    tickCounter++;
                }
            }

            // kill console
            consoleThread.shutdown();

            // kill network
            netController.stopListening();

            log.info("The server was safely shut down.");
            System.exit(0);
        }
    }

    public void banPlayerName(String name) {
        // TODO Auto-generated method stub

    }

    public void whitelistPlayerName(String name) {
        // TODO Auto-generated method stub

    }

    public void handlePlayerDisconnect(MainPlayer player) {
        // remove from playerlist
        PlayerListItemPacket playerListPacket = new OutputPlayerListItemPacket(null,
                player.getName(), false, (short) 0);
        playerListPacket.send();
        try {
            ((MainWorld) player.getWorld()).getEntityManager().removePlayer(player);
            onlinePlayers.remove(player);

            // call event
            PlayerQuitEvent event = new PlayerQuitEvent(player, new StringBuilder(
                    ChatColor.YELLOW.toString()).append(player.getDisplayName())
                    .append(" left the game.").toString());
            this.getPluginManager().callEvent(event);

            if ((event.getQuitMessage() != null) && (!event.getQuitMessage().isEmpty()))
                this.broadcastMessage(event.getQuitMessage());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void unbanPlayerName(String name) {
        // TODO Auto-generated method stub

    }

    public Set<MainPlayer> getOnlineMainPlayers() {
        return onlinePlayers;
    }

    public void broadcastPacket(OutputPacket packet) {
        netController.broadcastPacket(packet);
    }

    public Set<String> getListeningPluginChannels() {
        // TODO Auto-generated method stub
        return null;
    }

    public void sendPluginMessage(Plugin arg0, String arg1, byte[] arg2) {
        // TODO Auto-generated method stub

    }

    public Messenger getMessenger() {
        // TODO Auto-generated method stub
        return null;
    }
}
