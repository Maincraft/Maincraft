package me.main__.maincraft.packet;

public interface Packet {
    PacketClient getClient();

    int getOpcode();
}
