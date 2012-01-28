package me.main__.maincraft.packet.in;

public interface PlayerPositionAndLookPacket extends InputPacket {
    double getX();

    double getY();

    double getStance();

    double getZ();

    float getYaw();

    float getPitch();

    boolean isOnGround();
}
