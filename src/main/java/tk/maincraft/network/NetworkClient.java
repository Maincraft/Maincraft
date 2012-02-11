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

import tk.maincraft.MainServer;
import tk.maincraft.entity.MainPlayer;
import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.PacketClientAction;
import tk.maincraft.packet.PacketNotFoundException;
import tk.maincraft.packet.UnexpectedSocketIOException;
import tk.maincraft.packet.out.KeepAlivePacket;
import tk.maincraft.packet.out.KickPacket;
import tk.maincraft.packet.out.OutputPacket;
import tk.maincraft.packet.out.impl.OutputChatPacket;
import tk.maincraft.packet.out.impl.OutputKeepAlivePacket;
import tk.maincraft.packet.out.impl.OutputKickPacket;
import tk.maincraft.util.TextWrap;


public class NetworkClient implements PacketClient {

    private static final class SharedObject {
        private final Object obj;

        public SharedObject(NetworkClientActionAllInOne<?> task) {
            obj = task;
        }

        public SharedObject(OutputPacket packet) {
            obj = packet;
        }

        public boolean isTask() {
            return obj instanceof NetworkClientActionAllInOne<?>;
        }

        public boolean isPacket() {
            return obj instanceof OutputPacket;
        }

        public NetworkClientActionAllInOne<?> getTask() {
            return (NetworkClientActionAllInOne<?>) obj;
        }

        public OutputPacket getPacket() {
            return (OutputPacket) obj;
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

    public void send(OutputPacket packet) {
        send(packet, true);
    }

    public void send(final OutputPacket packet, final boolean autocatch) {
        send(packet, autocatch, false);
    }

    public void send(final OutputPacket packet, final boolean autocatch, boolean immediately) {
        if (!connected)
            throw new IllegalStateException("This client is not connected!");

        if (immediately) {
            doSend(packet, autocatch);
        }

        schedulePacket(packet);
    }

    private void doSend(OutputPacket packet, boolean autocatch) {
        try {
            PacketWriter.writePacket(this, packet);
        } catch (PacketNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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

    public DataInputStream getDataInputStream() {
        if (!connected)
            throw new IllegalStateException("This client is not connected!");

        return dataInputStream;
    }

    public DataOutputStream getDataOutputStream() {
        if (!connected)
            throw new IllegalStateException("This client is not connected!");

        return dataOutputStream;
    }

    public void kick(final String reason) {
        if (!connected)
            throw new IllegalStateException("This client is not connected!");

        scheduleClientAction(new PacketClientAction<Void>() {
            public Void call(PacketClient client) throws Exception {
                KickPacket kickPacket = new OutputKickPacket(client, reason);
                kickPacket.send();

                // add it to the end of the queue because our scheduled KickPacket has to be sent first!
                scheduleClientAction(new PacketClientAction<Void>() {
                    public Void call(PacketClient client) throws Exception {
                        client.disconnect();
                        return null;
                    }
                });
                return null;
            }
        });
    }

    public void kickNow(String reason) {
        KickPacket kickPacket = new OutputKickPacket(this, reason);
        doSend(kickPacket, true);
    }

    public boolean isConnected() {
        return connected;
    }

    public InetAddress getIp() {
        return socket.getInetAddress();
    }

    public String getIpString() {
        return socket.getInetAddress().getHostAddress();
    }

    public void keepAlive() throws UnexpectedSocketIOException {
        if (keepAliveQueue.size() > 50) {
            // that guy appears to have lost his connection
            this.disconnect();
        }

        if (isConnected() && !socket.isClosed() && socket.isConnected()) {
            int token = MainServer.rand.nextInt();
            KeepAlivePacket packet = new OutputKeepAlivePacket(this, token);
            keepAliveQueue.add(token);
            packet.send(false); // we want this to throw us out of everything if I/O fails!
        }
    }

    public boolean handleKeepAlive(int token) {
        if (!keepAliveQueue.contains(token))
            return false;
        else
            keepAliveQueue.remove(keepAliveQueue.indexOf(token));

        Bukkit.getLogger().finest("keptAlive: " + token);

        return true;
    }

    public void chat(String message) {
        // we're wrapping the message HERE.
        List<String> messages = TextWrap.wrapText(message);
        for (String msg : messages)
            schedulePacket(new OutputChatPacket(this, msg));
    }

    public void schedulePacket(OutputPacket packet) {
        queue.add(new SharedObject(packet));
    }

    public <T> Future<T> scheduleClientAction(PacketClientAction<T> action) {
        // *sigh* do I really want some kind of scheduler here?
        // yes. why? 'cause it's awesome :D

        NetworkClientActionAllInOne<T> newTask = new NetworkClientActionAllInOne<T>(this, action);
        queue.add(new SharedObject(newTask));
        return newTask;
    }

    public void processClientActions() {
        SharedObject obj = null;
        while (((obj = queue.poll()) != null) && connected) {
            if (obj.isPacket())
                doSend(obj.getPacket(), false);
            else if (obj.isTask())
                obj.getTask().run();
        }
    }

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

    public void setPlayer(MainPlayer player) {
        this.player = player;
    }

    public MainPlayer getPlayer() {
        return player;
    }
}
