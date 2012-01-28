package me.main__.maincraft.packet.in;

public interface PlayerPositionPacket extends InputPacket {
    double getX();

    double getY();

    double getStance();

    double getZ();

    boolean isOnGround();
}
