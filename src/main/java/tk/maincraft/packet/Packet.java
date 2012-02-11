package tk.maincraft.packet;

public interface Packet {
    PacketClient getClient();

    int getOpcode();
}
