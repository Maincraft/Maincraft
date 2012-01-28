package me.main__.maincraft.packet.out.impl.senders;

import java.io.IOException;
import java.util.zip.Deflater;

import org.bukkit.Bukkit;

import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.out.MapChunkPacket;
import me.main__.maincraft.world.MainChunk;

public final class MapChunkPacketSender extends AbstractPacketSender<MapChunkPacket> {
    @Override
    public void sendData(PacketClient client, MapChunkPacket packet) throws IOException {
        client.getDataOutputStream().writeInt(packet.getX());
        client.getDataOutputStream().writeShort(packet.getY());
        client.getDataOutputStream().writeInt(packet.getZ());

        client.getDataOutputStream().writeByte(packet.getSize_X() - 1);
        client.getDataOutputStream().writeByte(packet.getSize_Y() - 1);
        client.getDataOutputStream().writeByte(packet.getSize_Z() - 1);

        byte[] data = packet.getUncompressedChunkData();
        byte[] compressedData = new byte[(int) (MainChunk.SIZE_X * MainChunk.SIZE_Z
                * MainChunk.HEIGHT * (5D / 2D))];

        Deflater deflater = new Deflater(Deflater.BEST_SPEED);
        deflater.setInput(data);
        deflater.finish(); // It took me days to figure out that I forgot this single statement... RAGE!!!

        int compressed = deflater.deflate(compressedData);
        try {
            if (compressed == 0) {
                throw new IOException("failed to deflate");
            }
        }
        finally {
            deflater.end();
        }

        Bukkit.getLogger().finest("MapChunkPacketSender: Sending bytes: " + compressed);
        client.getDataOutputStream().writeInt(compressed);
        client.getDataOutputStream().write(compressedData, 0, compressed);
    }
}
