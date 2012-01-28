package me.main__.maincraft.packet.out;

import me.main__.maincraft.packet.Packet;

public interface OutputPacket extends Packet {
    void send();

    void send(boolean autocatch);
}
