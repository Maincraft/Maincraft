package me.main__.maincraft.packet.in;

import me.main__.maincraft.MainServer;
import me.main__.maincraft.packet.Packet;

public interface InputPacket extends Packet {
    void process(MainServer server);
}
