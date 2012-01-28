package me.main__.maincraft.packet.out;

public interface MapChunkPacket extends OutputPacket {
    int getX();

    short getY();

    int getZ();

    int getSize_X();

    int getSize_Y();

    int getSize_Z();

    // it's compressed in the PacketSender
    byte[] getUncompressedChunkData();
}
