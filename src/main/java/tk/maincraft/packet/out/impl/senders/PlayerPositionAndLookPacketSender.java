package tk.maincraft.packet.out.impl.senders;

import java.io.IOException;

import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.out.PlayerPositionAndLookPacket;


public final class PlayerPositionAndLookPacketSender extends AbstractPacketSender<PlayerPositionAndLookPacket> {
    @Override
    public void sendData(PacketClient client, PlayerPositionAndLookPacket packet)
            throws IOException {
        client.getDataOutputStream().writeDouble(packet.getX());
        client.getDataOutputStream().writeDouble(packet.getY());
        client.getDataOutputStream().writeDouble(packet.getStance());
        client.getDataOutputStream().writeDouble(packet.getZ());
        client.getDataOutputStream().writeFloat(packet.getYaw());
        client.getDataOutputStream().writeFloat(packet.getPitch());
        client.getDataOutputStream().writeBoolean(packet.isOnGround());
    }
}
