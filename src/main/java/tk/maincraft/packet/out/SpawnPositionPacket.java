package tk.maincraft.packet.out;

public interface SpawnPositionPacket extends OutputPacket {
    int getX();

    int getY();

    int getZ();
}
