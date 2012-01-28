package me.main__.maincraft.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.util.concurrent.Future;

import me.main__.maincraft.entity.MainPlayer;
import me.main__.maincraft.packet.Packet;
import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.PacketClientAction;
import me.main__.maincraft.packet.out.OutputPacket;
import me.main__.maincraft.packet.out.PacketSender;

public class FileDebugClient implements PacketClient {

    private DataOutputStream dataOutputStream;

    public FileDebugClient(FileOutputStream stream) {
        dataOutputStream = new DataOutputStream(stream);
    }

    public void send(OutputPacket packet) {
        try {
            PacketSender<? extends Packet> genericSender = PacketSenderFactory
                    .getNewPacketSender(packet.getOpcode());
            @SuppressWarnings("unchecked")
            PacketSender<Packet> sender = (PacketSender<Packet>) genericSender;
            sender.send(this, packet);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void send(OutputPacket packet, boolean autocatch) {
        send(packet);
    }

    public void send(OutputPacket packet, boolean autocatch, boolean immediately) {
        send(packet);
    }

    public DataInputStream getDataInputStream() {
        throw new UnsupportedOperationException();
    }

    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    public InetAddress getIp() {
        throw new UnsupportedOperationException();
    }

    public String getIpString() {
        throw new UnsupportedOperationException();
    }

    public void kick(final String reason) {
        throw new UnsupportedOperationException();
    }

    public void chat(String message) {
        throw new UnsupportedOperationException();
    }

    public void keepAlive() {
        throw new UnsupportedOperationException();
    }

    public boolean handleKeepAlive(int token) {
        throw new UnsupportedOperationException();
    }

    public boolean isConnected() {
        return true;
    }

    public void processClientActions() {
        throw new UnsupportedOperationException();
    }

    public <T> Future<T> scheduleClientAction(PacketClientAction<T> action) {
        throw new UnsupportedOperationException();
    }

    public void disconnect() {
        throw new UnsupportedOperationException();
    }

    public void setPlayer(MainPlayer player) {
        throw new UnsupportedOperationException();
    }

    public MainPlayer getPlayer() {
        throw new UnsupportedOperationException();
    }

}
