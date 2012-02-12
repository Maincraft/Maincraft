package tk.maincraft.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


import org.bukkit.Bukkit;

import tk.maincraft.MainServer;
import tk.maincraft.packet.PacketNotFoundException;
import tk.maincraft.packet.UnexpectedSocketIOException;
import tk.maincraft.packet.in.InputPacket;
import tk.maincraft.packet.out.OutputPacket;

public class NetworkController {

    private final int port;
    private final MainServer server;

    private boolean listening;
    private ServerSocket socket;
    private ExecutorService threadPool;

    private final Map<UUID, NetworkClient> clients = new ConcurrentHashMap<UUID, NetworkClient>();

    public NetworkController(int port, MainServer server) {
        this.port = port;
        this.server = server;
        listening = false;
    }

    public void startListening() throws IOException {
        listening = true;

        socket = new ServerSocket(port);
        socket.setSoTimeout(1000);
        threadPool = Executors.newCachedThreadPool();

        // Create the ServerThread
        threadPool.submit(new ServerThread());
    }

    public void stopListeningNOW() {
        // tell the serverthread to finish
        listening = false;

        // tell the clients to finish
        for (NetworkClient nc : clients.values()) {
            nc.kickNow("Stopping server!");
        }

        threadPool.shutdownNow();
    }

    public void stopListening() {
        try {
            // tell the serverthread to finish
            listening = false;

            // tell the clients to finish
            for (NetworkClient nc : clients.values()) {
                nc.kick("Stopping server!");
            }

            threadPool.shutdown();
            // we're IN a thread of the threadpool because it's the stop-command ...
            // ... this is going to take some time...
            threadPool.awaitTermination(1, TimeUnit.SECONDS);
            threadPool.shutdownNow();
        } catch (InterruptedException e) {
        }
    }

    public void broadcastPacket(OutputPacket packet) {
        for (NetworkClient nc : clients.values()) {
            nc.schedulePacket(packet);
        }
    }

    public InetAddress getMyIP() {
        return socket.getInetAddress();
    }

    public String getMyIPString() {
        return socket.getInetAddress().getHostName();
    }

    public boolean isListening() {
        return listening;
    }

    private class ServerThread implements Runnable {
        public ServerThread() {
            Bukkit.getLogger().info("yay! serverthread!");
        }

        public void run() {
            while (listening) {
                try {
                    Socket clisocket = socket.accept();
                    if (listening)
                        threadPool.submit(new ClientHandlingThread(clisocket));
                    else
                        clisocket.close(); // Sorry, but we're stopping
                } catch (SocketTimeoutException ste) {
                } catch (Throwable t) {
                    // TODO auto-generated catch block
                    t.printStackTrace();
                }
            }
        }
    }

    private class ClientHandlingThread implements Runnable {
        private final Socket clientsocket;
        private final UUID id;
        private final NetworkClient client;

        public ClientHandlingThread(Socket socket) throws IOException {
            this.clientsocket = socket;
            this.id = UUID.randomUUID();
            client = new NetworkClient(clientsocket);
            clients.put(id, client);
        }

        public void run() {
            try {
                Bukkit.getLogger().info("Handling client");

                clientsocket.setSoTimeout(1000); // 1 sec timeout

                while (client.isConnected() && client.getSocket().isConnected()
                        && !client.getSocket().isClosed()) {
                    Bukkit.getLogger().finer("Clienthandling-iteration");
                    InputPacket packet;
                    try {
                        packet = PacketReader.read(client);
                    } catch (PacketNotFoundException e) {
                        e.printStackTrace();
                        continue; // we want to record which packets we need
                    }

                    if (packet != null)
                        packet.process(server);

                    if ((MainServer.rand.nextInt() % 15) == 1)
                        client.keepAlive();

                    client.processClientActions();
                }
            } catch (UnexpectedSocketIOException e) {
                Bukkit.getLogger().info(
                        String.format("UnexpectedSocketIOException for client %s", clientsocket
                                .getRemoteSocketAddress().toString()));
            } catch (PacketNotFoundException e) {
                client.kickNow("Packet not found -.-");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                client.disconnect();
                if (client.getPlayer() != null)
                    server.handlePlayerDisconnect(client.getPlayer());
                client.setPlayer(null);
                if (!(clients.remove(id) == client))
                    throw new RuntimeException("That client got lost...");

                Bukkit.getLogger().info("Client handling finished.");
            }
        }

    }
}
