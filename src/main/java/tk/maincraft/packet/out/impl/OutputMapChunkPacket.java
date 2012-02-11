package tk.maincraft.packet.out.impl;

import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.out.MapChunkPacket;

public class OutputMapChunkPacket extends AbstractOutputPacket implements MapChunkPacket {

    private final int x;
    private final short y;
    private final int z;
    private final int size_x;
    private final int size_y;
    private final int size_z;
    private final byte[] chunkData;

    public OutputMapChunkPacket(PacketClient client, int x, short y, int z, int size_x, int size_y, int size_z, byte[] chunkData) {
        super(client);
        this.x = x;
        this.y = y;
        this.z = z;
        this.size_x = size_x;
        this.size_y = size_y;
        this.size_z = size_z;
        this.chunkData = chunkData;
    }

    @Override
    public int getOpcode() {
        return 0x33;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public short getY() {
        return y;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public int getSize_X() {
        return size_x;
    }

    @Override
    public int getSize_Y() {
        return size_y;
    }

    @Override
    public int getSize_Z() {
        return size_z;
    }

    @Override
    public byte[] getUncompressedChunkData() {
        return chunkData;
    }

    public String getToStringDescription() {
        return String
                .format("x=\"%d\",y=\"%d\",z=\"%d\",size_x=\"%d\",size_y=\"%d\",size_z=\"%d\",chunkData=byte[%d]",
                        x, y, z, size_x, size_y, size_z, chunkData.length);
    }

}
