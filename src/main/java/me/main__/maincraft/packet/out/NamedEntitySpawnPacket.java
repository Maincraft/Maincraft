package me.main__.maincraft.packet.out;

public interface NamedEntitySpawnPacket extends OutputPacket {
    int getEntityId();

    String getPlayerName();

    int getX();

    int getY();

    int getZ();

    byte getYaw();

    byte getPitch();

    short getCurrentItem();
}
