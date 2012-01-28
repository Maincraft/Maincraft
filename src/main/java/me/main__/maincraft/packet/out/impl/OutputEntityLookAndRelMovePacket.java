package me.main__.maincraft.packet.out.impl;

import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.out.EntityLookAndRelMovePacket;

public class OutputEntityLookAndRelMovePacket extends AbstractOutputPacket implements
        EntityLookAndRelMovePacket {
    private final int entityId;
    private final byte diffX;
    private final byte diffY;
    private final byte diffZ;
    private final byte yaw;
    private final byte pitch;

    public OutputEntityLookAndRelMovePacket(PacketClient client, int entityId, byte diffX, byte diffY, byte diffZ, byte yaw, byte pitch) {
        super(client);
        this.entityId = entityId;
        this.diffX = diffX;
        this.diffY = diffY;
        this.diffZ = diffZ;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public int getOpcode() {
        return 0x21;
    }

    @Override
    public int getEntityId() {
        return entityId;
    }

    @Override
    public byte getDiffX() {
        return diffX;
    }

    @Override
    public byte getDiffY() {
        return diffY;
    }

    @Override
    public byte getDiffZ() {
        return diffZ;
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
    public String getToStringDescription() {
        return String.format(
                "entityId=\"%d\",diffX=\"%d\",diffY=\"%d\",diffZ=\"%d\",yaw=\"%d\",pitch=\"%d\"",
                entityId, diffX, diffY, diffZ, yaw, pitch);
    }

}
