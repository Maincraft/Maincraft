package tk.maincraft.world;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;

import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;

import tk.maincraft.network.PacketClient;
import tk.maincraft.util.mcpackets.packet.MapChunkPacket;
import tk.maincraft.util.mcpackets.packet.impl.MapChunkPacketImpl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class MainChunk implements Chunk {
    public static final int SIZE_X = 16, SIZE_Z = 16, HEIGHT = 256;
    public static final int SIZE = SIZE_X * SIZE_Z * HEIGHT;

    private final MainWorld world;
    
    private final ChunkCoords coords;

    private byte[] types;
    private byte[] metaData;
    private byte[] blockLight;
    private byte[] skyLight;
    // maybe we'll use a cache instead
//    private Cache<BlockLocation, MainBlock> blockCache = CacheBuilder.newBuilder().weakValues()
//            .removalListener(new RemovalListener<BlockLocation, MainBlock>() {
//                public void onRemoval(RemovalNotification<BlockLocation, MainBlock> arg0) {
//                    // TO-DO Auto-generated method stub
//                    
//                }
//            })
//            .build(new CacheLoader<BlockLocation, MainBlock>() {
//                public MainBlock load(BlockLocation arg0) throws Exception {
//                    // TO-DO Auto-generated method stub
//                    return null;
//                }
//            });
//    private MainBlock[][][] blocks;

    private static final LoadingCache<MainChunk, MapChunkPacket> CHUNK_PACKET_CACHE =
            CacheBuilder.newBuilder().weakKeys()
            .build(new CacheLoader<MainChunk, MapChunkPacket>() {
        @Override
        public MapChunkPacket load(MainChunk chunk) throws Exception {
            return chunk.createPacket();
        }
    });
    
//    private static final ExecutorService threads = Executors.newFixedThreadPool(10);

    private MainChunk(final MainWorld world, final ChunkCoords coords) {
        this.world = world;
        this.coords = coords;
    }
    
    public MainChunk(final MainWorld world, final ChunkCoords coords, byte[] types) {
        this(world, coords);

        if (types.length != SIZE)
            throw new IllegalArgumentException();
        
//        this.blocks = new MainBlock[SIZE_X][SIZE_Z][HEIGHT];
        this.types = types;
        this.metaData = new byte[SIZE];
        this.skyLight = new byte[SIZE];
        this.blockLight = new byte[SIZE];

        // let's set skyLight and blockLight to 15... for whatever reason
        for (int i = 0; i < skyLight.length; i++) {
            skyLight[i] = 15;
            blockLight[i] = 15;
        }
        
//        threads.submit(new Runnable() {
//            public void run() {
//                long millisBefore = System.currentTimeMillis();
//                for (int x = 0; x < SIZE_X; x++) {
//                    for (int z = 0; z < SIZE_Z; z++) {
//                        for (int y = 0; y < HEIGHT; y++) {
//                            BlockLocation loc = new BlockLocation(world, coords, x, y, z);
//                            MetadataCollection metadata = new BlockMetadataCollection();
//                            //blocks[x][z][y] = new MainBlock(MainChunk.this, loc, metadata);
//                        }
//                    }
//                }
//                long millisAfter = System.currentTimeMillis();
//                long diff = millisAfter - millisBefore;
//                System.err.println("-------- TOOK " + diff + "ms");
//            }
//        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getX() {
        return coords.x;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getZ() {
        return coords.z;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public World getWorld() {
        return world;
    }

    public byte getType(int x, int y, int z) {
        return types[coordToIndex(x, z, y)];
    }

    public void setType(int x, int y, int z, byte type) {
        this.types[coordToIndex(x, z, y)] = type;
    }
    
    public byte getBlockLight(int x, int y, int z) {
        return blockLight[coordToIndex(x, z, y)];
    }
    
    public void setBlockLight(int x, int y, int z, byte blockLight) {
        this.blockLight[coordToIndex(x, z, y)] = blockLight;
    }
    
    public byte getSkyLight(int x, int y, int z) {
        return skyLight[coordToIndex(x, z, y)];
    }
    
    public void setSkyLight(int x, int y, int z, byte skyLight) {
        this.skyLight[coordToIndex(x, z, y)] = skyLight;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Block getBlock(int x, int y, int z) {
        //return blocks[x][z][y];
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChunkSnapshot getChunkSnapshot() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChunkSnapshot getChunkSnapshot(boolean includeMaxblocky, boolean includeBiome,
            boolean includeBiomeTempRain) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Entity[] getEntities() {
        // TODO Auto-generated method stub
        return new Entity[0];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BlockState[] getTileEntities() {
        // TODO Auto-generated method stub
        return new BlockState[0];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLoaded() {
        return world.isChunkLoaded(coords.x, coords.z);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean load(boolean generate) {
        return world.loadChunk(coords.x, coords.z, generate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean load() {
        return world.loadChunk(coords.x, coords.z, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean unload(boolean save, boolean safe) {
        return world.unloadChunk(coords.x, coords.z, save, safe);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean unload(boolean save) {
        return world.unloadChunk(coords.x, coords.z, save);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean unload() {
        return world.unloadChunk(this);
    }

    public static int coordToIndex(int x, int z, int y) {
        if (x < 0 || z < 0 || y < 0 || x >= SIZE_X || z >= SIZE_Z || y >= HEIGHT)
            throw new IndexOutOfBoundsException();

        // TODO verify that this is right
        return (y * SIZE_X + x) * SIZE_Z + z;
    }

    public void send(PacketClient client) {
        client.send(CHUNK_PACKET_CACHE.getUnchecked(this));
        // maybe add a change-queue
    }

    private MapChunkPacket createPacket() {
        // (types + metaData + blocklight + skylight + add) * 16 vanilla-chunks + biome
        byte[] data = new byte[(4096 + 2048 + 2048 + 2048 + 0) * 16 + 256];

        int pos = 0; //types.length;

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < SIZE_X; x++) {
                for (int z = 0; z < SIZE_Z; z++) {
                    data[pos++] = types[coordToIndex(x, z, y)];
                }
            }
        }

        if (pos != types.length) {
            throw new IllegalStateException("Illegal pos: " + pos + " vs " + types.length);
        }

        boolean hasValue = false;
        byte otherVal = 0;
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < SIZE_X; x++) {
                for (int z = 0; z < SIZE_Z; z++) {
                    byte val = metaData[coordToIndex(x, z, y)];
                    if (hasValue) {
                        data[pos++] = (byte) ((val << 4) | otherVal);
                        hasValue = false;
                    } else {
                        otherVal = val;
                        hasValue = true;
                    }
                }
            }
        }

        hasValue = false;
        otherVal = 0;
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < SIZE_X; x++) {
                for (int z = 0; z < SIZE_Z; z++) {
                    byte val = blockLight[coordToIndex(x, z, y)];
                    if (hasValue) {
                        data[pos++] = (byte) ((val << 4) | otherVal);
                        hasValue = false;
                    } else {
                        otherVal = val;
                        hasValue = true;
                    }
                }
            }
        }

        hasValue = false;
        otherVal = 0;
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < SIZE_X; x++) {
                for (int z = 0; z < SIZE_Z; z++) {
                    byte val = skyLight[coordToIndex(x, z, y)];
                    if (hasValue) {
                        data[pos++] = (byte) ((val << 4) | otherVal);
                        hasValue = false;
                    } else {
                        otherVal = val;
                        hasValue = true;
                    }
                }
            }
        }
//        for (int i = 0; i < metaData.length; i += 2) {
//            byte meta1 = metaData[i];
//            byte meta2 = metaData[i + 1];
//            data[pos++] = (byte) ((meta2 << 4) | meta1);
//        }
//
//        for (int i = 0; i < blockLight.length; i += 2) {
//            byte light1 = blockLight[i];
//            byte light2 = blockLight[i + 1];
//            data[pos++] = (byte) ((light2 << 4) | light1);
//        }
//
//        for (int i = 0; i < skyLight.length; i += 2) {
//            byte light1 = skyLight[i];
//            byte light2 = skyLight[i + 1];
//            data[pos++] = (byte) ((light2 << 4) | light1);
//        }

        for (int i = 0; i < 256; i++) {
            data[pos++] = 0; // biome data, just set it to 0
        }

        if (pos != data.length) {
            throw new IllegalStateException("Illegal Pos: " + pos + " vs " + data.length);
        }

        int primaryBitMap = 0xFFFF; // everything set to 1; TODO do not send air
        int addBitMap = 0; // should be unused

        Deflater deflater = new Deflater(Deflater.DEFAULT_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();
        byte[] compressed = new byte[data.length];
        int length = deflater.deflate(compressed);
        deflater.end();
        byte[] realCompressed = new byte[length];
        for (int i = 0; i < length; i++) {
            realCompressed[i] = compressed[i];
        }
        return new MapChunkPacketImpl(coords.x, coords.z, true, primaryBitMap, addBitMap, realCompressed);
    }

    private static interface Reader<T> {
        T read(DataInputStream stream) throws IOException;
    }

    private static interface Writer<T> {
        void write(DataOutputStream stream, T data, SerializedData type) throws IOException;
    }

    private static interface Processor<T> extends Reader<T>, Writer<T> {
    }

    private static class ByteArrayProcessor implements Processor<byte[]> {
        private static final ByteArrayProcessor instance = new ByteArrayProcessor();

        @Override
        public byte[] read(DataInputStream stream) throws IOException {
            int length = stream.readInt();
            byte[] data = new byte[length];
            int read = stream.read(data, 0, length);
            if (read != length) throw new IOException("Not fully read :("); 
            return data;
        }

        @Override
        public void write(DataOutputStream stream, byte[] data, SerializedData type) throws IOException {
            stream.writeByte(type.getKey());
            stream.writeInt(data.length);
            stream.write(data);
        }

        public static ByteArrayProcessor get() {
            return instance;
        }
    }

    private static enum SerializedData {
        BYTE_TYPES((byte) 0x0, ByteArrayProcessor.get()),
        BYTE_METADATA((byte) 0x1, ByteArrayProcessor.get()),
        BYTE_SKYLIGHT((byte) 0x2, ByteArrayProcessor.get()),
        BYTE_BLOCKLIGHT((byte) 0x3, ByteArrayProcessor.get()),
        OLD_BYTE_TYPES((byte) 0x4, ByteArrayProcessor.get());

        private static final SerializedData[] lookupArray;

        private final byte key;
        private final Reader<?> reader;
        private final Writer<?> writer;

        private SerializedData(byte key, Processor<?> processor) {
            this(key, processor, processor);
        }

        private SerializedData(byte key, Reader<?> reader, Writer<?> writer) {
            this.key = key;
            this.reader = reader;
            this.writer = writer;
        }

        public byte getKey() {
            return key;
        }

        @SuppressWarnings("unchecked")
        public <T> void write(DataOutputStream stream, T data) throws IOException {
            ((Writer<? super T>) this.writer).write(stream, data, this);
        }

        @SuppressWarnings("unchecked")
        public <T> T read(DataInputStream stream) throws IOException {
            return ((Reader<? extends T>) this.reader).read(stream);
        }

        public static SerializedData fromKey(int key) {
            return lookupArray[key];
        }

        static {
            int highest = -1;
            for (SerializedData sd : SerializedData.values()) {
                if (sd.getKey() > highest)
                    highest = sd.getKey();
            }
            lookupArray = new SerializedData[highest + 1];
            for (SerializedData sd : SerializedData.values()) {
                lookupArray[sd.getKey()] = sd;
            }
        }
    }

    public void save(OutputStream stream) throws IOException {
        DataOutputStream dStream = new DataOutputStream(stream);

        SerializedData.BYTE_TYPES.write(dStream, types);
        SerializedData.BYTE_METADATA.write(dStream, metaData);
        SerializedData.BYTE_SKYLIGHT.write(dStream, skyLight);
        SerializedData.BYTE_BLOCKLIGHT.write(dStream, blockLight);

        dStream.close();
    }

    public static MainChunk load(InputStream stream, MainWorld world, ChunkCoords coords) throws IOException {
        DataInputStream dStream = new DataInputStream(stream);
        MainChunk newChunk = new MainChunk(world, coords);
        
        while (true) {
            int read = dStream.read();
            if (read == -1)
                break;
            else {
                SerializedData sd = SerializedData.fromKey(read);
                // this is it
                Object readData = sd.read(dStream);
                switch (sd) {
                case OLD_BYTE_TYPES:
                case BYTE_TYPES:
                    newChunk.types = (byte[]) readData;
                    System.out.println("init'd types");
                    break;
                case BYTE_METADATA:
                    newChunk.metaData = (byte[]) readData;
                    System.out.println("init'd metadata");
                    break;
                case BYTE_BLOCKLIGHT:
                    newChunk.blockLight = (byte[]) readData;
                    System.out.println("init'd blocklight");
                    break;
                case BYTE_SKYLIGHT:
                    newChunk.skyLight = (byte[]) readData;
                    System.out.println("init'd skylight");
                    break;
                default:
                    throw new UnsupportedOperationException("Invalid entry found: " + sd);
                }
            }
        }
        System.out.println("finished chunk-loading");

        return newChunk;
    }
    
    @Override
    protected void finalize() throws Throwable {
        System.out.println("finalizing");
        world.saveChunk(this);
        System.out.println("finalized");
        super.finalize();
    }
}
