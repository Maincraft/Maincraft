package tk.maincraft.world;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

import tk.maincraft.MainServer;
import tk.maincraft.Maincraft;
import tk.maincraft.entity.MainPlayer;

public class MainWorld implements World {
    private final Environment environment;
    private long seed;
    private boolean pvp;
    private boolean keepSpawnInMemory;
    private boolean autoSave;
    private Difficulty difficulty = Difficulty.PEACEFUL;
    private final String name;
    @Deprecated
    private final long longId;
    @Deprecated
    private static long LONG_ID_COUNTER = 0;
    private final UUID uid;

    private final MainServer server;
    private final EntityManager entityManager;
    //private final DoubleKeyMap<Integer, Integer, MainChunk> chunkMap = new GenericDoubleKeyMap<Integer, Integer, MainChunk>(HashMap.class);
    private static final int CHUNK_MEMORY_TIME = Maincraft.getServer().getConfig().getIOSettings().getKeepChunkInMemoryTime();
    
    private static final ReferenceQueue<MainChunk> chunkUnloadQueue = new ReferenceQueue<MainChunk>();
    private LinkedList<WeakReference<MainChunk>> refList = new LinkedList<WeakReference<MainChunk>>();
    static {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Reference<? extends MainChunk> ref = chunkUnloadQueue.remove();
                        System.err.println("chunkUNload: " + ref.get());
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    
    void saveChunk(MainChunk chunk) {
        System.err.println(chunk);
        
        if (chunk != null) {
            System.out.println("juhu");
            File chunkFolder = new File(getWorldFolder(), "chunks");
            File chunkFile = new File(chunkFolder, String.format("%d__%d.mainchunk", chunk.getX(), chunk.getZ()));
            System.out.println(chunkFile.getAbsolutePath());
            try {
                chunkFile.delete();
                chunkFile.createNewFile();
                FileOutputStream fStream = new FileOutputStream(chunkFile);
//                ObjectOutputStream oStream = new ObjectOutputStream(fStream);
//                oStream.writeObject(chunk);
//                oStream.flush();
//                oStream.close();
                chunk.save(fStream);
                fStream.flush();
                fStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    private final LoadingCache<ChunkCoords, MainChunk> chunkMap = CacheBuilder.newBuilder().softValues()
            //.expireAfterAccess(CHUNK_MEMORY_TIME, TimeUnit.MINUTES)
            .removalListener(new RemovalListener<ChunkCoords, MainChunk>() {
                @Override
                public void onRemoval(RemovalNotification<ChunkCoords, MainChunk> notification) {
                    // TODO maybe saving?
                    //MainChunk chunk = notification.getValue();
                    System.err.println(notification.getCause().toString());
                    //saveChunk(chunk);
                }
            })
            .build(new CacheLoader<ChunkCoords, MainChunk>() {
                @Override
                public MainChunk load(ChunkCoords coords) throws Exception {
                    File chunkFolder = new File(getWorldFolder(), "chunks");
                    File chunkFile = new File(chunkFolder, String.format("%d__%d.mainchunk", coords.x, coords.z));
                    if (chunkFile.exists()) {
                        try {
                            FileInputStream fStream = new FileInputStream(chunkFile);
//                            ObjectInputStream oStream = new ObjectInputStream(fStream);
//                            Object oChunk = oStream.readObject();
//                            if (oChunk instanceof MainChunk) {
//                                return (MainChunk) oChunk;
//                            } else {
//                                server.getLogger().severe("SERIOUSLY!? NOT THE RIGHT CLASS!?");
//                            }
                            MainChunk myChunk = MainChunk.load(fStream, MainWorld.this, coords);
                            fStream.close();
                            System.err.println("========================================================");
                            System.err.println("READ CHUNK!!!");
                            return myChunk;
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    //System.err.println("GENERATING NEW CHUNK");
//                    gens++;
                    int x = coords.x;
                    int z = coords.z;
                    // TODO implement some actual chunk-loading here since we currently only generate it
                    Random r = new Random();
                    // r.setSeed(0); // TODO seeds
                    byte[] types = chunkGenerator.generate(MainWorld.this, r, x, z);
                    MainChunk chunk = new MainChunk(MainWorld.this, coords, types);
                    
                    // add to queue
                    refList.add(new WeakReference<MainChunk>(chunk, chunkUnloadQueue));
                    
                    return chunk;
                }
            });
    private final ChunkGenerator chunkGenerator;
    
    //public int gens = 0;
    //public int dels = 0;

    public CacheStats getCacheStats() {
        return chunkMap.stats();
    }

    public long getNumberOfLoadedChunks() {
        return chunkMap.size();
    }

    public void cleanupChunkMap() {
        chunkMap.cleanUp();
    }

    public MainWorld(MainServer server, String name, Environment env, ChunkGenerator chunkGenerator) {
        this.server = server;
        this.environment = env;
        this.name = name;
        uid = UUID.randomUUID(); // TODO real uuid
        this.chunkGenerator = chunkGenerator;

        this.longId = LONG_ID_COUNTER++;
        
        entityManager = new EntityManager(this.server, this);
    }

    public static final int normalCoordToChunkCoord(int normal) {
        // we would just divide this by 16, but this is faster/better:
        return (normal >> 4);
    }
    
    public static final int normalCoordToInnerChunkCoord(int normal) {
        // all this does is modulo 16
        return normal & 0xF;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Block getBlockAt(int x, int y, int z) {
        int chunkX = normalCoordToChunkCoord(x);
        int chunkZ = normalCoordToChunkCoord(z);

        return getChunkAt(chunkX, chunkZ).getBlock(normalCoordToInnerChunkCoord(x),
                y & 0x7F /* all this does is modulo 128 */, normalCoordToInnerChunkCoord(z));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Block getBlockAt(Location location) {
        if (!(location.getWorld() != this))
            throw new IllegalArgumentException("Location must be in this world!");

        return getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getBlockTypeIdAt(int x, int y, int z) {
        return getBlockAt(x, y, z).getTypeId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getBlockTypeIdAt(Location location) {
        return getBlockAt(location).getTypeId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getHighestBlockYAt(int x, int z) {
        return getHighestBlockAt(x, z).getY();
    }

    /**
     * {@inheritDoc}
     */
    /**
     * {@inheritDoc}
     */
    @Override
    public int getHighestBlockYAt(Location location) {
        return getHighestBlockYAt(location.getBlockX(), location.getBlockZ());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Block getHighestBlockAt(int x, int z) {
        Block highest = this.getBlockAt(x, 0, z);
        for (int y = 0; y < MainChunk.HEIGHT; y++) {
            Block block = this.getBlockAt(x, y, z);
            if (block.getType() != Material.AIR)
                highest = block;
        }
        return highest;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Block getHighestBlockAt(Location location) {
        return getHighestBlockAt(location.getBlockX(), location.getBlockZ());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Chunk getChunkAt(int x, int z) {
        loadChunk(x, z);

        try {
            return chunkMap.get(new ChunkCoords(x, z));
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Chunk getChunkAt(Location location) {
        return getChunkAt(normalCoordToChunkCoord(location.getBlockX()),
                normalCoordToChunkCoord(location.getBlockZ()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Chunk getChunkAt(Block block) {
        return getChunkAt(block.getLocation());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isChunkLoaded(Chunk chunk) {
        if (!(chunk instanceof MainChunk))
            return false; // I wonder where that guy got that chunk-object from...

        return chunkMap.asMap().containsValue((MainChunk) chunk);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Chunk[] getLoadedChunks() {
        return new Chunk[0]; //chunkMap.asMap().values().toArray(new Chunk[0]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isChunkLoaded(int x, int z) {
        return chunkMap.asMap().containsKey(new ChunkCoords(x, z));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadChunk(Chunk chunk) {
        loadChunk(chunk.getX(), chunk.getZ());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadChunk(int x, int z) {
        loadChunk(x, z, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean loadChunk(int x, int z, boolean generate) {
        // TODO this
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean unloadChunk(Chunk chunk) {
        return unloadChunk(chunk.getX(), chunk.getZ());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean unloadChunk(int x, int z) {
        return unloadChunk(x, z, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean unloadChunk(int x, int z, boolean save) {
        return unloadChunk(x, z, save, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean unloadChunk(int x, int z, boolean save, boolean safe) {
        ChunkCoords coords = new ChunkCoords(x, z);
        // check safety
        if (safe) {
            for (MainPlayer player : server.getOnlineMainPlayers()) {
                if (player.getClientView().isChunkPreChunked(coords))
                    return false;
            }
        }
        chunkMap.invalidate(coords);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean unloadChunkRequest(int x, int z) {
        return unloadChunk(x, z);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean unloadChunkRequest(int x, int z, boolean safe) {
        return unloadChunk(x, z, true, safe);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean regenerateChunk(int x, int z) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean refreshChunk(int x, int z) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Item dropItem(Location location, ItemStack item) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Item dropItemNaturally(Location location, ItemStack item) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Arrow spawnArrow(Location location, Vector velocity, float speed, float spread) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean generateTree(Location location, TreeType type) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean generateTree(Location loc, TreeType type, BlockChangeDelegate delegate) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LivingEntity spawnCreature(Location loc, CreatureType type) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LightningStrike strikeLightning(Location loc) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LightningStrike strikeLightningEffect(Location loc) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Entity> getEntities() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LivingEntity> getLivingEntities() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Player> getPlayers() {
        return entityManager.getPlayers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID getUID() {
        return uid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public long getId() {
        return longId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Location getSpawnLocation() {
        return new Location(this, 0, 100, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean setSpawnLocation(int x, int y, int z) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getTime() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTime(long time) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getFullTime() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFullTime(long time) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasStorm() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStorm(boolean hasStorm) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getWeatherDuration() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setWeatherDuration(int duration) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isThundering() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setThundering(boolean thundering) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getThunderDuration() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setThunderDuration(int duration) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean createExplosion(double x, double y, double z, float power) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean createExplosion(Location loc, float power) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean createExplosion(Location loc, float power, boolean setFire) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getSeed() {
        return seed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getPVP() {
        return pvp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPVP(boolean pvp) {
        this.pvp = pvp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChunkGenerator getGenerator() {
        return chunkGenerator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save() {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BlockPopulator> getPopulators() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Entity> T spawn(Location location, Class<T> clazz)
            throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void playEffect(Location location, Effect effect, int data) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void playEffect(Location location, Effect effect, int data, int radius) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChunkSnapshot getEmptyChunkSnapshot(int x, int z, boolean includeBiome,
            boolean includeBiomeTempRain) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSpawnFlags(boolean allowMonsters, boolean allowAnimals) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getAllowAnimals() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getAllowMonsters() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Biome getBiome(int x, int z) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getTemperature(int x, int z) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getHumidity(int x, int z) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxHeight() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSeaLevel() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getKeepSpawnInMemory() {
        return keepSpawnInMemory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setKeepSpawnInMemory(boolean keepLoaded) {
        this.keepSpawnInMemory = keepLoaded;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAutoSave() {
        return autoSave;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAutoSave(boolean value) {
        this.autoSave = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File getWorldFolder() {
        // TODO Auto-generated method stub
        return null;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getListeningPluginChannels() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendPluginMessage(Plugin arg0, String arg1, byte[] arg2) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T>... arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WorldType getWorldType() {
        // TODO Auto-generated method stub
        return null;
    }

}
