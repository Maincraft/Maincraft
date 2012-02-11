package tk.maincraft.packet.out.impl;

import org.bukkit.Location;

import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.out.EntityTeleportPacket;
import tk.maincraft.util.RotationUtils;


public class OutputEntityTeleportPacket extends AbstractOutputPacket implements
        EntityTeleportPacket {
    private final int entityId;
    private final int x;
    private final int y;
    private final int z;
    private final byte yaw;
    private final byte pitch;

    public OutputEntityTeleportPacket(PacketClient client, int entityId, Location loc) {
        this(client, entityId, ((int) loc.getX() * 32), ((int) loc.getY() * 32),
                ((int) loc.getZ() * 32), loc.getYaw(), loc.getPitch());
    }

    public OutputEntityTeleportPacket(PacketClient client, int entityId, int x, int y, int z, float yaw, float pitch) {
        this(client, entityId, x, y, z, RotationUtils.floatToByte(yaw), RotationUtils
                .floatToByte(pitch));
    }

    public OutputEntityTeleportPacket(PacketClient client, int entityId, int x, int y, int z, byte yaw, byte pitch) {
        super(client);
        this.entityId = entityId;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public int getOpcode() {
        return 0x22;
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
        return String.format("entityId=\"%d\",x=\"%d\",y=\"%d\",z=\"%d\",yaw=\"%d\",pitch=\"%d\"",
                entityId, x, y, z, yaw, pitch);
    }

}
