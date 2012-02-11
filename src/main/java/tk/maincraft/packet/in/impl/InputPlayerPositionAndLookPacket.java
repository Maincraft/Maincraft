package tk.maincraft.packet.in.impl;

import org.bukkit.Location;

import tk.maincraft.MainServer;
import tk.maincraft.entity.MainPlayer;
import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.in.PlayerPositionAndLookPacket;
import tk.maincraft.world.MainWorld;


public class InputPlayerPositionAndLookPacket extends AbstractInputPacket implements
        PlayerPositionAndLookPacket {

    private final double x;
    private final double y;
    private final double stance;
    private final double z;
    private final float yaw;
    private final float pitch;
    private final boolean onGround;

    public InputPlayerPositionAndLookPacket(PacketClient client, Location loc, double stance, boolean onGround) {
        this(client, loc.getX(), loc.getY(), loc.getZ(), stance, loc.getYaw(), loc.getPitch(),
                onGround);
    }

    public InputPlayerPositionAndLookPacket(PacketClient client, double x, double y, double z, double stance, float yaw, float pitch, boolean onGround) {
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
    public void process(MainServer server) {
        MainPlayer player = client.getPlayer();
        MainWorld world = (MainWorld) player.getWorld();
        world.getEntityManager().handlePlayerMoveAndLook(player, getLocation());
        world.getEntityManager().handlePlayerOnGround(player, onGround);
    }

    @Override
    public int getOpcode() {
        return 0x0D; // packet 13 / 0x0D
    }

    public Location getLocation() {
        return new Location(client.getPlayer().getWorld(), x, y, z, yaw, pitch);
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
