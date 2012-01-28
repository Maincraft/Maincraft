package me.main__.maincraft.packet.out.impl;

import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.out.EntityLookPacket;

public class OutputEntityLookPacket extends AbstractOutputPacket implements EntityLookPacket {
    private final int entityId;
    private final byte yaw;
    private final byte pitch;

    public OutputEntityLookPacket(PacketClient client, int entityId, byte yaw, byte pitch) {
        super(client);
        this.entityId = entityId;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public int getOpcode() {
        return 0x20;
    }

    @Override
    public int getEntityId() {
        return entityId;
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
        return String.format("entityId=\"%d\",yaw=\"%d\",pitch=\"%d\"", entityId, yaw, pitch);
    }

}
