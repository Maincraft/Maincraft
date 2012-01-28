package me.main__.maincraft.packet.out.impl;

import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.out.NamedEntitySpawnPacket;

public class OutputNamedEntitySpawnPacket extends AbstractOutputPacket implements
        NamedEntitySpawnPacket {
    private final int entityId;
    private final String playerName;
    private final int x;
    private final int y;
    private final int z;
    private final byte yaw;
    private final byte pitch;
    private final short currentItem;

    public OutputNamedEntitySpawnPacket(PacketClient client, int entityId, String playerName, int x, int y, int z, byte yaw, byte pitch, short currentItem) {
        super(client);
        this.entityId = entityId;
        this.playerName = playerName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.currentItem = currentItem;
    }

    public int getOpcode() {
        return 0x14;
    }

    @Override
    public int getEntityId() {
        return entityId;
    }

    @Override
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public byte getYaw() {
        return yaw;
    }

    @Override
    public byte getPitch() {
        return pitch;
    }

    @Override
    public short getCurrentItem() {
        return currentItem;
    }

    @Override
    public String getToStringDescription() {
        return String
                .format("entityId=\"%d\",playerName=\"%s\",x=\"%d\",y=\"%d\",z=\"%d\",yaw=\"%d\",pitch=\"%d\",currentItem=\"%d\"",
                        entityId, playerName, x, y, z, yaw, pitch, currentItem);
    }

}
