package me.main__.maincraft.packet.out;

public interface LoginPacket extends OutputPacket {
    int getEntityID();

    long getSeed();

    int getMode();

    byte getDimension();

    byte getDifficulty();

    byte getWorldHeight();

    byte getMaxPlayers();
}
