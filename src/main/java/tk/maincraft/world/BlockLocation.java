package tk.maincraft.world;

import static tk.maincraft.world.MainWorld.normalCoordToInnerChunkCoord;

import org.bukkit.Location;

public class BlockLocation {
    private final MainWorld world;
    private final ChunkCoords chunk;
    private final int x;
    private final int y;
    private final int z;

    public BlockLocation(MainWorld world, ChunkCoords chunk, int x, int y, int z) {
        this.world = world;
        this.chunk = chunk;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public MainWorld getWorld() {
        return world;
    }

    public ChunkCoords getChunk() {
        return chunk;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getInnerX() {
        return normalCoordToInnerChunkCoord(x);
    }

    public int getInnerY() {
        return normalCoordToInnerChunkCoord(y);
    }

    public int getInneZ() {
        return normalCoordToInnerChunkCoord(z);
    }
    
    public Location toLocation() {
        return new Location(world, x, y, z);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((chunk == null) ? 0 : chunk.hashCode());
        result = prime * result + x;
        result = prime * result + y;
        result = prime * result + z;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof BlockLocation))
            return false;
        BlockLocation other = (BlockLocation) obj;
        if (chunk == null) {
            if (other.chunk != null)
                return false;
        }
        else if (!chunk.equals(other.chunk))
            return false;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        if (z != other.z)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return String.format("BlockLocation [world=%s, chunk=%s, x=%s, y=%s, z=%s]",
                world, chunk, x, y, z);
    }
}
