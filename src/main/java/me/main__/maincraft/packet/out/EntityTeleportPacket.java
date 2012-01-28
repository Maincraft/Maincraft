package me.main__.maincraft.packet.out;

public interface EntityTeleportPacket extends OutputPacket {
    int getEntityId();

    int getX();

    int getY();

    int getZ();

    byte getYaw();

    byte getPitch();
}
