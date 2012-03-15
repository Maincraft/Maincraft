package tk.maincraft.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.server.ServerListPingEvent;

import tk.maincraft.MainServer;
import tk.maincraft.Maincraft;
import tk.maincraft.entity.MainPlayer;
import tk.maincraft.util.TextWrap;
import tk.maincraft.util.Versioning;
import tk.maincraft.util.mcpackets.Packet;
import tk.maincraft.util.mcpackets.PacketReader;
import tk.maincraft.util.mcpackets.PacketWriter;
import tk.maincraft.util.mcpackets.UnexpectedSocketIOException;
import tk.maincraft.util.mcpackets.packet.*;
import tk.maincraft.util.mcpackets.packet.impl.ChatPacketImpl;
import tk.maincraft.util.mcpackets.packet.impl.HandshakePacketImpl;
import tk.maincraft.util.mcpackets.packet.impl.KeepAlivePacketImpl;
import tk.maincraft.util.mcpackets.packet.impl.KickPacketImpl;
import tk.maincraft.world.MainWorld;

public class NetworkClient implements PacketClient {
    private static final class SharedObject {
        private final Object obj;

        public SharedObject(NetworkClientActionAllInOne<?> task) {
            obj = task;
        }

        public SharedObject(Packet packet) {
            obj = packet;
        }

        public boolean isTask() {
            return obj instanceof NetworkClientActionAllInOne<?>;
        }

        public boolean isPacket() {
            return obj instanceof Packet;
        }

        public NetworkClientActionAllInOne<?> getTask() {
            return (NetworkClientActionAllInOne<?>) obj;
        }

        public Packet getPacket() {
            return (Packet) obj;
        }
    }

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    private boolean connected;
    private List<Integer> keepAliveQueue = new LinkedList<Integer>();

    private MainPlayer player = null;

    // private final Queue<NetworkClientActionAllInOne<?>> tasks = new ConcurrentLinkedQueue<NetworkClientActionAllInOne<?>>();
    // private final Queue<OutputPacket> packetQueue = new ConcurrentLinkedQueue<OutputPacket>();
    private final Queue<SharedObject> queue = new ConcurrentLinkedQueue<SharedObject>();

    public NetworkClient(Socket socket) throws IOException {
        this.socket = socket;
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());

        connected = true;
    }

    @Override
    public void send(Packet packet) {
        queue.add(new SharedObject(packet));
    }

    @Override
    public void send(Packet... packets) {
        for (Packet packet : packets)
            this.send(packet);
    };

    @Override
    public void readAndHandleOnePacket() {
        try {
            handlePacket(PacketReader.read(dataInputStream));
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnexpectedSocketIOException(-1, e);
        }
    }

    @Override
    public void handlePacket(Packet packet) {
        try {
            System.out.println("HANDLE: " + packet);
            this.getClass().getMethod("handlePacket", packet.getPacketType()).invoke(this, packet);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void handlePacket(ChatPacket packet) {
        this.player.chat(packet.getMessage());
    }

    public void handlePacket(HandshakePacket packet) {
        // TODO auth
        this.send(new HandshakePacketImpl("-"));
    }

    public void handlePacket(KeepAlivePacket packet) {
        if (!keepAliveQueue.contains(packet.getToken()))
            throw new IllegalArgumentException("Illegal keepAlive-token: " + packet.getToken());
        else keepAliveQueue.remove(keepAliveQueue.indexOf(packet.getToken()));

        Bukkit.getLogger().finest("keptAlive: " + packet.getToken());
    }

    public void handlePacket(KickPacket packet) {
        Bukkit.getLogger().info(String.format("Player '%s' disconnected: %s",
                player.getName(), packet.getReason()));
        this.disconnect();
    }

    public void handlePacket(LoginPacket packet) {
        // check if protocol versions match
        if (packet.getProtocolVersionOrEntityId() != Versioning.getMinecraftProtocolVersion()) {
            this.kick((packet.getProtocolVersionOrEntityId() > Versioning.getMinecraftProtocolVersion())
                    ? "Outdated server! (main() is lazy ;))" : "Outdated client!");
            return;
        }

        // Forward login to server
        if (!Maincraft.getServer().tryLoginPlayer(packet.getUsername(), this)) {
            this.kick("The server doesn't want you - for whatever reason.");
            return;
        }

        // the answer-login-packet is sent in server.tryLoginPlayer()
    }

    public void handlePacket(PlayerOnGroundPacket packet) {
        MainWorld world = (MainWorld) player.getWorld();
        world.getEntityManager().handlePlayerOnGround(player, packet.getOnGround());
    }

    public void handlePacket(PlayerLookPacket packet) {
        MainWorld world = (MainWorld) player.getWorld();
        world.getEntityManager().handlePlayerLook(player, packet.getPitch(), packet.getYaw());
        world.getEntityManager().handlePlayerOnGround(player, packet.getOnGround());
    }

    public void handlePacket(PlayerPositionPacket packet) {
        MainWorld world = (MainWorld) player.getWorld();
        // construct location
        Location loc = new Location(world, packet.getX(), packet.getY(), packet.getZ());
        world.getEntityManager().handlePlayerMove(player, loc);
        world.getEntityManager().handlePlayerOnGround(player, packet.getOnGround());
    }

    public void handlePacket(PlayerPositionAndLookPacket packet) {
        MainWorld world = (MainWorld) player.getWorld();
        // construct location
        Location loc = new Location(world, packet.getX(), packet.getY(), packet.getZ(),
                packet.getYaw(), packet.getPitch());
        world.getEntityManager().handlePlayerMoveAndLook(player, loc);
        world.getEntityManager().handlePlayerOnGround(player, packet.getOnGround());
    }

    public void handlePacket(ServerListPingPacket packet) {
        MainServer server = Maincraft.getServer();
        ServerListPingEvent event = new ServerListPingEvent(this.getIp(), server.getMOTD(),
                server.getOnlinePlayers().length, server.getMaxPlayers());
        server.getPluginManager().callEvent(event);

        StringBuilder message = new StringBuilder();
        message.append(event.getMotd()).append("\247");
        message.append(event.getNumPlayers()).append("\247");
        message.append(event.getMaxPlayers());

        this.kick(message.toString());
    }

    private void doSend(Packet packet, boolean autocatch) {
        try {
            PacketWriter.writePacket(dataOutputStream, packet);
        } catch (UnexpectedSocketIOException e) {
            if (!autocatch)
                throw e;
            else
                e.printStackTrace();
        }
    }

    public Socket getSocket() {
        if (!connected)
            throw new IllegalStateException("This client is not connected!");

        return socket;
    }

    @Override
    public void kick(final String reason) {
        if (!connected)
            throw new IllegalStateException("This client is not connected!");

        KickPacket kickPacket = new KickPacketImpl(reason);
        send(kickPacket);

        // add disconnect to the end of the queue because our scheduled packet has to be sent first!
        scheduleClientAction(new PacketClientAction<Void>() {
            @Override
            public Void call(PacketClient client) throws Exception {
                client.disconnect();
                return null;
            }
        });
    }

    public void kickNow(String reason) {
        KickPacket kickPacket = new KickPacketImpl(reason);
        doSend(kickPacket, true);
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public InetAddress getIp() {
        return socket.getInetAddress();
    }

    @Override
    public String getIpString() {
        return socket.getInetAddress().getHostAddress();
    }

    @Override
    public void keepAlive() throws UnexpectedSocketIOException {
        if (keepAliveQueue.size() > 50) {
            // that guy appears to have lost his connection
            this.disconnect();
        }

        if (isConnected() && !socket.isClosed() && socket.isConnected()) {
            int token = MainServer.rand.nextInt();
            KeepAlivePacket packet = new KeepAlivePacketImpl(token);
            keepAliveQueue.add(token);
            doSend(packet, false); // we want this to throw us out of everything if I/O fails!
        }
    }

    @Override
    public void chat(String message) {
        // we're wrapping the message HERE.
        List<String> messages = TextWrap.wrapText(message);
        for (String msg : messages)
            send(new ChatPacketImpl(msg));
    }

    @Override
    public <T> Future<T> scheduleClientAction(PacketClientAction<T> action) {
        // *sigh* do I really want some kind of scheduler here?
        // yes. why? 'cause it's awesome :D
        NetworkClientActionAllInOne<T> newTask = new NetworkClientActionAllInOne<T>(this, action);
        queue.add(new SharedObject(newTask));
        return newTask;
    }

    @Override
    public void processClientActions() {
        SharedObject obj = null;
        while (((obj = queue.poll()) != null) && connected) {
            if (obj.isPacket()) {
                Packet packet = obj.getPacket();
                doSend(packet, false);
            } else if (obj.isTask()) {
                obj.getTask().run();
            } else {
                throw new UnsupportedOperationException();
            }
        }
    }

    @Override
    public void disconnect() {
        try {
            this.dataInputStream.close();
            this.dataOutputStream.close();
        } catch (SocketException se) {
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            this.socket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        connected = false;
    }

    @Override
    public void setPlayer(MainPlayer player) {
        this.player = player;
    }

    @Override
    public MainPlayer getPlayer() {
        return player;
    }
}
