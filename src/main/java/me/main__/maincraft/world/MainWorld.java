package me.main__.maincraft.world;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import me.main__.maincraft.MainServer;
import me.main__.maincraft.entity.MainPlayer;
import me.main__.maincraft.util.doublekeymap.DoubleKeyMap;
import me.main__.maincraft.util.doublekeymap.GenericDoubleKeyMap;

import org.bukkit.BlockChangeDelegate;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Difficulty;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class MainWorld implements World {

    private final Environment environment;
    private long seed;
    private boolean pvp;
    private boolean keepSpawnInMemory;
    private boolean autoSave;
    private Difficulty difficulty = Difficulty.PEACEFUL;
    private final String name;
    private final UUID uid;

    private final MainServer server;
    private final EntityManager entityManager;
    private final DoubleKeyMap<Integer, Integer, MainChunk> chunkMap = new GenericDoubleKeyMap<Integer, Integer, MainChunk>(
            HashMap.class);
    private final ChunkGenerator chunkGenerator;

    public MainWorld(MainServer server, String name, Environment env, ChunkGenerator chunkGenerator) {
        this.server = server;
        this.environment = env;
        this.name = name;
        uid = UUID.randomUUID(); // TODO real uuid
        this.chunkGenerator = chunkGenerator;

        entityManager = new EntityManager(this.server, this);
    }

    public static final int normalCoordToChunkCoord(int normal) {
        // we would just divide this by 16, but this is faster/better:
        return (normal >> 4);
    }

    public Block getBlockAt(int x, int y, int z) {
        int chunkX = normalCoordToChunkCoord(x);
        int chunkZ = normalCoordToChunkCoord(z);

        return getChunkAt(chunkX, chunkZ).getBlock(x & 0xF /* all this does is modulo 16 */,
                y & 0x7F /* all this does is modulo 128 */, z & 0xF);
    }

    public Block getBlockAt(Location location) {
        if (!(location.getWorld() != this))
            throw new IllegalArgumentException("Location must be in this world!");

        return getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public int getBlockTypeIdAt(int x, int y, int z) {
        return getBlockAt(x, y, z).getTypeId();
    }

    public int getBlockTypeIdAt(Location location) {
        return getBlockAt(location).getTypeId();
    }

    public int getHighestBlockYAt(int x, int z) {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getHighestBlockYAt(Location location) {
        // TODO Auto-generated method stub
        return 0;
    }

    public Block getHighestBlockAt(int x, int z) {
        // TODO Auto-generated method stub
        return null;
    }

    public Block getHighestBlockAt(Location location) {
        // TODO Auto-generated method stub
        return null;
    }

    public Chunk getChunkAt(int x, int z) {
        loadChunk(x, z);

        return chunkMap.get(x, z);
    }

    public Chunk getChunkAt(Location location) {
        return getChunkAt(normalCoordToChunkCoord(location.getBlockX()),
                normalCoordToChunkCoord(location.getBlockZ()));
    }

    public Chunk getChunkAt(Block block) {
        return getChunkAt(block.getLocation());
    }

    public boolean isChunkLoaded(Chunk chunk) {
        if (!(chunk instanceof MainChunk))
            return false; // I wonder where that guy got that chunk-object from...

        return chunkMap.contains((MainChunk) chunk);
    }

    public Chunk[] getLoadedChunks() {
        return chunkMap.getValues().toArray(new Chunk[0]);
    }

    public boolean isChunkLoaded(int x, int z) {
        return chunkMap.contains(x, z);
    }

    public void loadChunk(Chunk chunk) {
        loadChunk(chunk.getX(), chunk.getZ());
    }

    public void loadChunk(int x, int z) {
        loadChunk(x, z, true);
    }

    public boolean loadChunk(int x, int z, boolean generate) {
        // TODO implement some actual chunk-loading here since we currently only generate it
        Random r = new Random();
        // r.setSeed(0); // TODO
        byte[] types = chunkGenerator.generate(this, r, x, z);
        MainChunk chunk = new MainChunk(this, x, z, types);
        chunkMap.put(x, z, chunk);
        return true;
    }

    public boolean unloadChunk(Chunk chunk) {
        return unloadChunk(chunk.getX(), chunk.getZ());
    }

    public boolean unloadChunk(int x, int z) {
        return unloadChunk(x, z, true);
    }

    public boolean unloadChunk(int x, int z, boolean save) {
        return unloadChunk(x, z, save, true);
    }

    public boolean unloadChunk(int x, int z, boolean save, boolean safe) {
        // check safety
        if (safe) {
            ChunkCoords coords = new ChunkCoords(x, z);
            for (MainPlayer player : server.getOnlineMainPlayers()) {
                if (player.getClientView().isChunkPreChunked(coords))
                    return false;
            }
        }
        chunkMap.remove(x, z);
        // this should be enough until we implement saving (TODO)!
        return true;
    }

    public boolean unloadChunkRequest(int x, int z) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean unloadChunkRequest(int x, int z, boolean safe) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean regenerateChunk(int x, int z) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean refreshChunk(int x, int z) {
        // TODO Auto-generated method stub
        return false;
    }

    public Item dropItem(Location location, ItemStack item) {
        // TODO Auto-generated method stub
        return null;
    }

    public Item dropItemNaturally(Location location, ItemStack item) {
        // TODO Auto-generated method stub
        return null;
    }

    public Arrow spawnArrow(Location location, Vector velocity, float speed, float spread) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean generateTree(Location location, TreeType type) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean generateTree(Location loc, TreeType type, BlockChangeDelegate delegate) {
        // TODO Auto-generated method stub
        return false;
    }

    public LivingEntity spawnCreature(Location loc, CreatureType type) {
        // TODO Auto-generated method stub
        return null;
    }

    public LightningStrike strikeLightning(Location loc) {
        // TODO Auto-generated method stub
        return null;
    }

    public LightningStrike strikeLightningEffect(Location loc) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<Entity> getEntities() {
        // TODO Auto-generated method stub
        return null;
    }

    public List<LivingEntity> getLivingEntities() {
        // TODO Auto-generated method stub
        return null;
    }

    public List<Player> getPlayers() {
        return entityManager.getPlayers();
    }

    public String getName() {
        return name;
    }

    public UUID getUID() {
        return uid;
    }

    public long getId() {
        // TODO Auto-generated method stub
        return 0;
    }

    public Location getSpawnLocation() {
        return new Location(this, 0, 100, 0);
    }

    public boolean setSpawnLocation(int x, int y, int z) {
        // TODO Auto-generated method stub
        return false;
    }

    public long getTime() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void setTime(long time) {
        // TODO Auto-generated method stub

    }

    public long getFullTime() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void setFullTime(long time) {
        // TODO Auto-generated method stub

    }

    public boolean hasStorm() {
        // TODO Auto-generated method stub
        return false;
    }

    public void setStorm(boolean hasStorm) {
        // TODO Auto-generated method stub

    }

    public int getWeatherDuration() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void setWeatherDuration(int duration) {
        // TODO Auto-generated method stub

    }

    public boolean isThundering() {
        // TODO Auto-generated method stub
        return false;
    }

    public void setThundering(boolean thundering) {
        // TODO Auto-generated method stub

    }

    public int getThunderDuration() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void setThunderDuration(int duration) {
        // TODO Auto-generated method stub

    }

    public boolean createExplosion(double x, double y, double z, float power) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean createExplosion(double x, double y, double z, float power, boolean setFire) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean createExplosion(Location loc, float power) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean createExplosion(Location loc, float power, boolean setFire) {
        // TODO Auto-generated method stub
        return false;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public long getSeed() {
        return seed;
    }

    public boolean getPVP() {
        return pvp;
    }

    public void setPVP(boolean pvp) {
        this.pvp = pvp;
    }

    public ChunkGenerator getGenerator() {
        return chunkGenerator;
    }

    public void save() {
        // TODO Auto-generated method stub

    }

    public List<BlockPopulator> getPopulators() {
        // TODO Auto-generated method stub
        return null;
    }

    public <T extends Entity> T spawn(Location location, Class<T> clazz)
            throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    public void playEffect(Location location, Effect effect, int data) {
        // TODO Auto-generated method stub

    }

    public void playEffect(Location location, Effect effect, int data, int radius) {
        // TODO Auto-generated method stub

    }

    public ChunkSnapshot getEmptyChunkSnapshot(int x, int z, boolean includeBiome,
            boolean includeBiomeTempRain) {
        // TODO Auto-generated method stub
        return null;
    }

    public void setSpawnFlags(boolean allowMonsters, boolean allowAnimals) {
        // TODO Auto-generated method stub

    }

    public boolean getAllowAnimals() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean getAllowMonsters() {
        // TODO Auto-generated method stub
        return false;
    }

    public Biome getBiome(int x, int z) {
        // TODO Auto-generated method stub
        return null;
    }

    public double getTemperature(int x, int z) {
        // TODO Auto-generated method stub
        return 0;
    }

    public double getHumidity(int x, int z) {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getMaxHeight() {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getSeaLevel() {
        // TODO Auto-generated method stub
        return 0;
    }

    public boolean getKeepSpawnInMemory() {
        return keepSpawnInMemory;
    }

    public void setKeepSpawnInMemory(boolean keepLoaded) {
        this.keepSpawnInMemory = keepLoaded;
    }

    public boolean isAutoSave() {
        return autoSave;
    }

    public void setAutoSave(boolean value) {
        this.autoSave = value;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public File getWorldFolder() {
        // TODO Auto-generated method stub
        return null;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

}
