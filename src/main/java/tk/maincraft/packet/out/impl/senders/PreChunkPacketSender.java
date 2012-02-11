package tk.maincraft.packet.out.impl.senders;

import java.io.IOException;

import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.out.PreChunkPacket;


public final class PreChunkPacketSender extends AbstractPacketSender<PreChunkPacket> {
    @Override
    public void sendData(PacketClient client, PreChunkPacket packet) throws IOException {
        client.getDataOutputStream().writeInt(packet.getX());
        client.getDataOutputStream().writeInt(packet.getZ());
        client.getDataOutputStream().writeBoolean(packet.getMode());
    }
}
