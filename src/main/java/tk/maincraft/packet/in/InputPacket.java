package tk.maincraft.packet.in;

import tk.maincraft.MainServer;
import tk.maincraft.packet.Packet;

public interface InputPacket extends Packet {
    void process(MainServer server);
}
