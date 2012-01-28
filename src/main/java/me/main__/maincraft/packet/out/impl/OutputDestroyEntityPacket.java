package me.main__.maincraft.packet.out.impl;

import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.out.DestroyEntityPacket;

public class OutputDestroyEntityPacket extends AbstractOutputPacket implements DestroyEntityPacket {
    private final int entityId;

    public OutputDestroyEntityPacket(PacketClient client, int entityId) {
        super(client);
        this.entityId = entityId;
    }

    public int getOpcode() {
        return 0x1D;
    }

    public int getEntityId() {
        return entityId;
    }

    public String getToStringDescription() {
        return String.format("entityId=\"%d\"", entityId);
    }

}
