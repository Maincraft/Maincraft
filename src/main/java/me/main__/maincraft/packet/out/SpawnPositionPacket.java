package me.main__.maincraft.packet.out;

public interface SpawnPositionPacket extends OutputPacket {
    int getX();

    int getY();

    int getZ();
}
