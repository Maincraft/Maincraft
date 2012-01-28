package me.main__.maincraft.packet.out.impl.senders;

import java.io.IOException;

import me.main__.maincraft.network.NetUtils;
import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.out.LoginPacket;

public final class LoginPacketSender extends AbstractPacketSender<LoginPacket> {
    @Override
    public void sendData(PacketClient client, LoginPacket packet) throws IOException {
        client.getDataOutputStream().writeInt(packet.getEntityID());
        NetUtils.writeString(client, ""); // playerName (unused)
        client.getDataOutputStream().writeLong(packet.getSeed());
        client.getDataOutputStream().writeInt(packet.getMode());
        client.getDataOutputStream().writeByte(packet.getDimension());
        client.getDataOutputStream().writeByte(packet.getDifficulty());
        client.getDataOutputStream().writeByte(packet.getWorldHeight());
        client.getDataOutputStream().writeByte(packet.getMaxPlayers());
    }
}
