package me.main__.maincraft.network;

import me.main__.maincraft.packet.PacketClient;

public interface PacketClientUsing {
    PacketClient getPacketClient();

    void setPacketClient(PacketClient pc);
}
