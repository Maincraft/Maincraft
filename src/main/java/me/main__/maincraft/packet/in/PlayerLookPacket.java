package me.main__.maincraft.packet.in;

public interface PlayerLookPacket extends InputPacket {
    float getYaw();

    float getPitch();

    boolean isOnGround();
}
