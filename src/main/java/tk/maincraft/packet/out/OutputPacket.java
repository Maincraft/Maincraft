package tk.maincraft.packet.out;

import tk.maincraft.packet.Packet;

public interface OutputPacket extends Packet {
    void send();

    void send(boolean autocatch);
}
