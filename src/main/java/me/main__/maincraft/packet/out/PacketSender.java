package me.main__.maincraft.packet.out;

import me.main__.maincraft.packet.Packet;
import me.main__.maincraft.packet.PacketClient;

public interface PacketSender<T extends Packet> {
    void send(PacketClient client, T packet);
}
