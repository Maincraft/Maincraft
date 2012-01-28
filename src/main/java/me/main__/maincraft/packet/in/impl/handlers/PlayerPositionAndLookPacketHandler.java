package me.main__.maincraft.packet.in.impl.handlers;

import java.io.IOException;

import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.in.PlayerPositionAndLookPacket;
import me.main__.maincraft.packet.in.impl.InputPlayerPositionAndLookPacket;

public final class PlayerPositionAndLookPacketHandler extends AbstractPacketHandler<PlayerPositionAndLookPacket> {
    @Override
    public PlayerPositionAndLookPacket doRead(PacketClient client) throws IOException {
        double x = client.getDataInputStream().readDouble();
        double y = client.getDataInputStream().readDouble();
        double stance = client.getDataInputStream().readDouble();
        double z = client.getDataInputStream().readDouble();
        float yaw = client.getDataInputStream().readFloat();
        float pitch = client.getDataInputStream().readFloat();
        boolean onGround = client.getDataInputStream().readBoolean();

        return new InputPlayerPositionAndLookPacket(client, x, y, z, stance, yaw, pitch, onGround);
    }
}