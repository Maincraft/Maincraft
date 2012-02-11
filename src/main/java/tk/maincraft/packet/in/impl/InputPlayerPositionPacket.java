package tk.maincraft.packet.in.impl;

import org.bukkit.Location;

import tk.maincraft.MainServer;
import tk.maincraft.entity.MainPlayer;
import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.in.PlayerPositionPacket;
import tk.maincraft.world.MainWorld;


public class InputPlayerPositionPacket extends AbstractInputPacket implements PlayerPositionPacket {

    private final double x;
    private final double y;
    private final double stance;
    private final double z;
    private final boolean onGround;

    public InputPlayerPositionPacket(PacketClient client, Location loc, double stance, boolean onGround) {
        this(client, loc.getX(), loc.getY(), loc.getZ(), stance, onGround);
    }

    public InputPlayerPositionPacket(PacketClient client, double x, double y, double z, double stance, boolean onGround) {
        super(client);
        this.x = x;
        this.y = y;
        this.stance = stance;
        this.z = z;
        this.onGround = onGround;
    }

    @Override
    public void process(MainServer server) {
        MainPlayer player = client.getPlayer();
        MainWorld world = (MainWorld) player.getWorld();
        world.getEntityManager().handlePlayerMove(player, getLocation());
        world.getEntityManager().handlePlayerOnGround(player, onGround);
    }

    public Location getLocation() {
        return new Location(client.getPlayer().getWorld(), x, y, z);
    }

    @Override
    public int getOpcode() {
        return 0x0B;
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
    public boolean isOnGround() {
        return onGround;
    }

    @Override
    public String getToStringDescription() {
        return String.format("x=\"%f\",y=\"%f\",stance=\"%f\",z=\"%f\",onGround=\"%b\"", x, y,
                stance, z, onGround);
    }
}
