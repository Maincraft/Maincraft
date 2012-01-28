package me.main__.maincraft.packet.out.impl;

import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.out.LoginPacket;

public class OutputLoginPacket extends AbstractOutputPacket implements LoginPacket {

    private int entityId;
    private long seed;
    private int mode;
    private byte dimension;
    private byte difficulty;
    private byte worldHeight;
    private byte maxPlayers;

    public OutputLoginPacket(PacketClient client, int entityId, long seed, int mode, byte dimension, byte difficulty, byte worldHeight, byte maxPlayers) {
        super(client);
        this.entityId = entityId;
        this.seed = seed;
        this.mode = mode;
        this.dimension = dimension;
        this.difficulty = difficulty;
        this.worldHeight = worldHeight;
        this.maxPlayers = maxPlayers;
    }

    @Override
    public int getEntityID() {
        return entityId;
    }

    @Override
    public long getSeed() {
        return seed;
    }

    @Override
    public int getMode() {
        return mode;
    }

    @Override
    public byte getDimension() {
        return dimension;
    }

    @Override
    public byte getDifficulty() {
        return difficulty;
    }

    @Override
    public byte getWorldHeight() {
        return worldHeight;
    }

    @Override
    public byte getMaxPlayers() {
        return maxPlayers;
    }

    @Override
    public int getOpcode() {
        return 1;
    }

    @Override
    public String getToStringDescription() {
        return String
                .format("entityId=\"%d\",seed=\"%d\",mode=\"%d\",dimension=\"%d\",difficulty=\"%d\",worldHeight=\"%d\",maxPlayers=\"%d\"",
                        entityId, seed, mode, dimension, difficulty, worldHeight, maxPlayers);
    }
}
