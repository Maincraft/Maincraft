package me.main__.maincraft.packet.in.impl.handlers;

import java.io.IOException;

import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.in.PlayerLookPacket;
import me.main__.maincraft.packet.in.impl.InputPlayerLookPacket;

public final class PlayerLookPacketHandler extends AbstractPacketHandler<PlayerLookPacket> {
    @Override
    public PlayerLookPacket doRead(PacketClient client) throws IOException {
        float yaw = client.getDataInputStream().readFloat();
        float pitch = client.getDataInputStream().readFloat();
        boolean onGround = client.getDataInputStream().readBoolean();

        return new InputPlayerLookPacket(client, yaw, pitch, onGround);
    }

}
