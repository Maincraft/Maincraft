package me.main__.maincraft.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.util.concurrent.Future;

import me.main__.maincraft.entity.MainPlayer;
import me.main__.maincraft.packet.out.OutputPacket;

public interface PacketClient {
    void send(OutputPacket packet);

    void send(OutputPacket packet, boolean autocatch);

    void send(OutputPacket abstractOutputPacket, boolean autocatch, boolean immediately);

    DataInputStream getDataInputStream();

    DataOutputStream getDataOutputStream();

    InetAddress getIp();

    String getIpString();

    void kick(String reason);

    void chat(String message);

    void keepAlive();

    boolean handleKeepAlive(int token);

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
