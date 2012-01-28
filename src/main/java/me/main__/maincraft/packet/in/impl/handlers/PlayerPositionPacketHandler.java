package me.main__.maincraft.packet.in.impl.handlers;

import java.io.IOException;

import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.in.PlayerPositionPacket;
import me.main__.maincraft.packet.in.impl.InputPlayerPositionPacket;

public final class PlayerPositionPacketHandler extends AbstractPacketHandler<PlayerPositionPacket> {
    @Override
    public PlayerPositionPacket doRead(PacketClient client) throws IOException {
        double x = client.getDataInputStream().readDouble();
        double y = client.getDataInputStream().readDouble();
        double stance = client.getDataInputStream().readDouble();
        double z = client.getDataInputStream().readDouble();
        boolean onGround = client.getDataInputStream().readBoolean();

        return new InputPlayerPositionPacket(client, x, y, z, stance, onGround);
    }
}
