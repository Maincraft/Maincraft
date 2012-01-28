package me.main__.maincraft.packet.in.impl.handlers;

import java.io.IOException;

import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.in.PlayerOnGroundPacket;
import me.main__.maincraft.packet.in.impl.InputPlayerOnGroundPacket;

public final class PlayerOnGroundPacketHandler extends AbstractPacketHandler<PlayerOnGroundPacket> {
    @Override
    public PlayerOnGroundPacket doRead(PacketClient client) throws IOException {
        boolean onGround = client.getDataInputStream().readBoolean();

        return new InputPlayerOnGroundPacket(client, onGround);
    }

}
