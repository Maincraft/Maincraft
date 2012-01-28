package me.main__.maincraft.packet.out.impl;

import org.bukkit.Location;

import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.out.PlayerPositionAndLookPacket;

public class OutputPlayerPositionAndLookPacket extends AbstractOutputPacket implements
        PlayerPositionAndLookPacket {

    private final double x;
    private final double y;
    private final double stance;
    private final double z;
    private final float yaw;
    private final float pitch;
    private final boolean onGround;

    public OutputPlayerPositionAndLookPacket(PacketClient client, Location loc, double stance, boolean onGround) {
        this(client, loc.getX(), loc.getY(), loc.getZ(), stance, loc.getYaw(), loc.getPitch(),
                onGround);
    }

    public OutputPlayerPositionAndLookPacket(PacketClient client, double x, double y, double z, double stance, float yaw, float pitch, boolean onGround) {
        super(client);
        this.x = x;
        this.y = y;
        this.stance = stance;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    @Override
    public int getOpcode() {
        return 0x0D; // packet 13 / 0x0D
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getStance() {
        return stance;
    }

    @Override
    public double getZ() {
        return z;
    }

    @Override
    public float getYaw() {
        return yaw;
    }

    @Override
    public float getPitch() {
        return pitch;
    }

    @Override
    public boolean isOnGround() {
        return onGround;
    }

    @Override
    public String getToStringDescription() {
        return String.format(
                "x=\"%f\",y=\"%f\",stance=\"%f\",z=\"%f\",yaw=\"%f\",pitch=\"%f\",onGround=\"%b\"",
                x, y, stance, z, yaw, pitch, onGround);
    }
}
