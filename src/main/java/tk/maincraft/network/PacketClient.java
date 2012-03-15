package tk.maincraft.network;

import java.net.InetAddress;
import java.util.concurrent.Future;

import tk.maincraft.entity.MainPlayer;
import tk.maincraft.util.mcpackets.Packet;


public interface PacketClient {
    void send(Packet packet);

    void send(Packet... packets);

    void handlePacket(Packet packet);

    void readAndHandleOnePacket();

    InetAddress getIp();

    String getIpString();

    void kick(String reason);

    void chat(String message);

    void keepAlive();

    boolean isConnected();

    /**
     * Beware! If you call this from anywhere else than the associated client thread, the wrath of the threading-gods will kill you!
     */
    void processClientActions();

    <T> Future<T> scheduleClientAction(PacketClientAction<T> action);

    void disconnect();

    void setPlayer(MainPlayer player);

    MainPlayer getPlayer();
}
