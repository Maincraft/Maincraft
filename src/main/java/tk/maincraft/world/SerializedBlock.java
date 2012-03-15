package tk.maincraft.world;

public class SerializedBlock {
    private final byte type;
    private final byte data;
    private final byte skyLight;
    private final byte blockLight;

    public SerializedBlock(byte type, byte data, byte skyLight, byte blockLight) {
        this.type = type;
        this.data = data;
        this.skyLight = skyLight;
        this.blockLight = blockLight;
    }
    
    public SerializedBlock(int type, byte data, byte skyLight, byte blockLight) {
        this((byte) type, data, skyLight, blockLight);
    }

    public byte getType() {
        return type;
    }

    public byte getData() {
        return data;
    }

    public byte getSkyLight() {
        return skyLight;
    }

    public byte getBlockLight() {
        return blockLight;
    }
}
