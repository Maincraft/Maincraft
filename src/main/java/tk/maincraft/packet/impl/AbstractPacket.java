package tk.maincraft.packet.impl;

import tk.maincraft.packet.Packet;
import tk.maincraft.packet.PacketClient;

public abstract class AbstractPacket implements Packet {
    protected final PacketClient client;

    public AbstractPacket(PacketClient client) {
        this.client = client;
    }

    @Override
    public PacketClient getClient() {
        return client;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName()).append("@").append(Integer.toHexString(hashCode()));
        sb.append(String.format(" (%1$d/0x0%1$X) ", getOpcode()));
        sb.append("[").append(getToStringDescription()).append("]");
        return sb.toString();
    }

    public abstract String getToStringDescription();

}
