package me.main__.maincraft.packet.out.impl;

import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.out.EntityRelMovePacket;

public class OutputEntityRelMovePacket extends AbstractOutputPacket implements EntityRelMovePacket {
    private final int entityId;
    private final byte diffX;
    private final byte diffY;
    private final byte diffZ;

    public OutputEntityRelMovePacket(PacketClient client, int entityId, byte diffX, byte diffY, byte diffZ) {
        super(client);
        this.entityId = entityId;
        this.diffX = diffX;
        this.diffY = diffY;
        this.diffZ = diffZ;
    }

    @Override
    public int getOpcode() {
        return 0x1F;
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
    public String getToStringDescription() {
        return String.format("entityId=\"%d\",diffX=\"%d\",diffY=\"%d\",diffZ=\"%d\"", entityId,
                diffX, diffY, diffZ);
    }

}
