package tk.maincraft.packet.out.impl;

import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.out.DestroyEntityPacket;

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
