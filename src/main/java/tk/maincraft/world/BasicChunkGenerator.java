package tk.maincraft.world;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public class BasicChunkGenerator extends ChunkGenerator {
    private final byte[] chunk = new byte[MainChunk.SIZE];
    private final int latency;
    private final Object lock = new Object();

    public BasicChunkGenerator(int latency) {
        this.latency = latency;
        for (int x = 0; x < MainChunk.SIZE_X; x++) {
            for (int z = 0; z < MainChunk.SIZE_Z; z++) {
                for (int y = 0; y < MainChunk.HEIGHT; y++) {
                    byte material;
                    if (y == 0)
                        material = (byte) Material.BEDROCK.getId();
                    else if (y < 65)
                        material = (byte) Material.GRASS.getId();
                    else
                        material = (byte) Material.AIR.getId();
                    chunk[MainChunk.coordToIndex(x, z, y)] = material;
                }
            }
        }
    }

    @Override
    public byte[] generate(World world, Random random, int cx, int cz) {
        if (latency > 0) {
            synchronized (lock) {
                try {
                    lock.wait(latency);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return chunk.clone();
    }
}
