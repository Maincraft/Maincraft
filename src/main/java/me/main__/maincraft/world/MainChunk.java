package me.main__.maincraft.world;

import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;

public class MainChunk implements Chunk {

    public static final int SIZE_X = 16, SIZE_Z = 16, HEIGHT = 128;

    private final MainWorld world;
    private final int x;
    private final int z;

    private byte[] types;
    private byte[] metaData;
    private byte[] skyLight;
    private byte[] blockLight;

    public MainChunk(MainWorld world, int x, int z, byte[] types) {
        if (!(types.length == SIZE_X * SIZE_Z * HEIGHT))
            throw new IllegalArgumentException();

        this.world = world;
        this.x = x;
        this.z = z;
        this.types = types;
        this.metaData = new byte[types.length];
        this.skyLight = new byte[types.length];
        this.blockLight = new byte[types.length];

        // let's set skyLight and blockLight to 15... for whatever reason
        for (int i = 0; i < skyLight.length; i++) {
            skyLight[i] = 15;
            blockLight[i] = 15;
        }
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public World getWorld() {
        return world;
    }

    public Block getBlock(int x, int y, int z) {
        // TODO Auto-generated method stub
        return null;
    }

    public ChunkSnapshot getChunkSnapshot() {
        // TODO Auto-generated method stub
        return null;
    }

    public ChunkSnapshot getChunkSnapshot(boolean includeMaxblocky, boolean includeBiome,
            boolean includeBiomeTempRain) {
        // TODO Auto-generated method stub
        return null;
    }

    public Entity[] getEntities() {
        // TODO Auto-generated method stub
        return new Entity[0];
    }

    public BlockState[] getTileEntities() {
        // TODO Auto-generated method stub
        return new BlockState[0];
    }

    public boolean isLoaded() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean load(boolean generate) {
        return world.loadChunk(x, z, generate);
    }

    public boolean load() {
        return world.loadChunk(x, z, true);
    }

    public boolean unload(boolean save, boolean safe) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean unload(boolean save) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean unload() {
        // TODO Auto-generated method stub
        return false;
    }

    public byte[] serializeToByteArray() {
        byte[] retval = new byte[(int) (SIZE_X * SIZE_Z * HEIGHT * (5D / 2D))];

        System.arraycopy(types, 0, retval, 0, types.length);

        int pos = types.length;

        // DEBUG:
        // System.out.println("retval " + retval.length + ", types " + types.length + ", metadata " + metaData.length + ", skyLight " + skyLight.length + ", blockLight " + blockLight.length);

        for (int i = 0; i < metaData.length; i += 2) {
            byte meta1 = metaData[i];
            byte meta2 = metaData[i + 1];
            retval[pos++] = (byte) ((meta2 << 4) | meta1);
        }

        for (int i = 0; i < skyLight.length; i += 2) {
            byte light1 = skyLight[i];
            byte light2 = skyLight[i + 1];
            retval[pos++] = (byte) ((light2 << 4) | light1);
        }

        for (int i = 0; i < blockLight.length; i += 2) {
            byte light1 = blockLight[i];
            byte light2 = blockLight[i + 1];
            retval[pos++] = (byte) ((light2 << 4) | light1);
        }

        return retval;
    }

}
