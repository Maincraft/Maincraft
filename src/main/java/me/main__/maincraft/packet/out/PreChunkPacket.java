package me.main__.maincraft.packet.out;

public interface PreChunkPacket extends OutputPacket {
    int getX();

    int getZ();

    boolean getMode();
}
