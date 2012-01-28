package me.main__.maincraft.packet.out.impl;

import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.out.PreChunkPacket;

public class OutputPreChunkPacket extends AbstractOutputPacket implements PreChunkPacket {

    private final int x;
    private final int z;
    private final boolean mode;

    public OutputPreChunkPacket(PacketClient client, int x, int z, boolean mode) {
        super(client);
        this.x = x;
        this.z = z;
        this.mode = mode;
    }

    @Override
    public int getOpcode() {
        return 0x32;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public boolean getMode() {
        return mode;
    }

    @Override
    public String getToStringDescription() {
        return String.format("x=\"%d\",z=\"%d\",mode=\"%b\"", x, z, mode);
    }

}
