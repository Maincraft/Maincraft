package tk.maincraft.packet.out.impl;

import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.out.KeepAlivePacket;

public class OutputKeepAlivePacket extends AbstractOutputPacket implements KeepAlivePacket {
    private final int id;

    public OutputKeepAlivePacket(PacketClient client, int id) {
        super(client);
        this.id = id;
    }

    @Override
    public int getOpcode() {
        return 0;
    }

    @Override
    public int getToken() {
        return id;
    }

    @Override
    public String getToStringDescription() {
        return String.format("id=\"%d\"", id);
    }
}
