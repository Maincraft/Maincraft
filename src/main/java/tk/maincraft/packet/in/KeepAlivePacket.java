package tk.maincraft.packet.in;

public interface KeepAlivePacket extends InputPacket {
    int getToken();
}
