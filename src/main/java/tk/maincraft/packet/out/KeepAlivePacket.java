package tk.maincraft.packet.out;

public interface KeepAlivePacket extends OutputPacket {
    int getToken();
}
