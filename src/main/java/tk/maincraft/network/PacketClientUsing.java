package tk.maincraft.network;

import tk.maincraft.packet.PacketClient;

public interface PacketClientUsing {
    PacketClient getPacketClient();

    void setPacketClient(PacketClient pc);
}
