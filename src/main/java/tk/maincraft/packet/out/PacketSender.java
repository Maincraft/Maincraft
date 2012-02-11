package tk.maincraft.packet.out;

import tk.maincraft.packet.Packet;
import tk.maincraft.packet.PacketClient;

public interface PacketSender<T extends Packet> {
    void send(PacketClient client, T packet);
}
