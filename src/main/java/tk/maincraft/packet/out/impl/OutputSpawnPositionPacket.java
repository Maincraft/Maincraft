package tk.maincraft.packet.out.impl;

import org.bukkit.Location;

import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.out.SpawnPositionPacket;


public class OutputSpawnPositionPacket extends AbstractOutputPacket implements SpawnPositionPacket {

    private final int x;
    private final int y;
    private final int z;

    public OutputSpawnPositionPacket(PacketClient client, Location spawnLocation) {
        this(client, spawnLocation.getBlockX(), spawnLocation.getBlockY(), spawnLocation
                .getBlockZ());
    }

    public OutputSpawnPositionPacket(PacketClient client, int x, int y, int z) {
        super(client);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public int getOpcode() {
        return 6;
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
    public String getToStringDescription() {
        return String.format("x=\"%d\",y=\"%d\",z=\"%d\"", x, y, z);
    }

}
