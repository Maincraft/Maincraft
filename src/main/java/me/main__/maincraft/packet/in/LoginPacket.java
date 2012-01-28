package me.main__.maincraft.packet.in;

public interface LoginPacket extends InputPacket {
    int getProtocolVersion();

    String getUsername();
}
