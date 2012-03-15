package tk.maincraft.world;

import java.util.Collection;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.inventory.ItemStack;

import tk.maincraft.world.metadata.MetadataCollection;
import tk.maincraft.world.metadata.EMetadataType;

public class MainBlock implements Block {
    private final MainChunk chunk;
    private final BlockLocation loc;
    private final MetadataCollection metadata;

    public MainBlock(MainChunk chunk, BlockLocation loc, MetadataCollection metadata) {
        this.chunk = chunk;
        this.loc = loc;
        this.metadata = metadata;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean breakNaturally() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean breakNaturally(ItemStack arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Biome getBiome() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getBlockPower() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getBlockPower(BlockFace face) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Chunk getChunk() {
        return chunk;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte getData() {
        return this.metadata.getMetadata(EMetadataType.DATA_VALUE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<ItemStack> getDrops() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<ItemStack> getDrops(ItemStack stack) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Block getFace(BlockFace face) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BlockFace getFace(Block face) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Block getFace(BlockFace face, int distance) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getHumidity() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte getLightFromBlocks() {
        return chunk.getBlockLight(getX(), getY(), getZ());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte getLightFromSky() {
        return chunk.getSkyLight(getX(), getY(), getZ());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte getLightLevel() {
        return (byte) Math.max(getLightFromSky(), getLightFromBlocks());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Location getLocation() {
        return loc.toLocation();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Block getRelative(BlockFace arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Block getRelative(BlockFace arg0, int arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Block getRelative(int arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BlockState getState() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getTemperature() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Material getType() {
        return Material.getMaterial(getTypeId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTypeId() {
        return chunk.getType(getX(), getY(), getZ());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public World getWorld() {
        return loc.getWorld();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getX() {
        return loc.getX();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getY() {
        return loc.getY();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getZ() {
        return loc.getZ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBlockFaceIndirectlyPowered(BlockFace face) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBlockFacePowered(BlockFace face) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBlockIndirectlyPowered() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBlockPowered() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return getType() == Material.AIR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLiquid() {
        return (getType() == Material.WATER) || (getType() == Material.LAVA)
                || (getType() == Material.STATIONARY_WATER) || (getType() == Material.STATIONARY_LAVA);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setData(byte arg0) {
        setData(arg0, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setData(byte data, boolean applyPhysics) {
        this.metadata.setMetadata(EMetadataType.DATA_VALUE, data);
        //chunk.setData(x, y, z,)
        // TODO physics
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setType(Material arg0) {
        setTypeId(arg0.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean setTypeId(int arg0) {
        return setTypeId(arg0, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean setTypeId(int arg0, boolean applyPhysics) {
        return setTypeIdAndData(arg0, (byte) 0, applyPhysics);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean setTypeIdAndData(int arg0, byte arg1, boolean arg2) {
        // TODO Auto-generated method stub
        return false;
    }
}
