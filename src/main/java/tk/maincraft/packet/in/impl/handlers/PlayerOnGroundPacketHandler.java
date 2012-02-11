package tk.maincraft.packet.in.impl.handlers;

import java.io.IOException;

import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.in.PlayerOnGroundPacket;
import tk.maincraft.packet.in.impl.InputPlayerOnGroundPacket;


public final class PlayerOnGroundPacketHandler extends AbstractPacketHandler<PlayerOnGroundPacket> {
    @Override
    public PlayerOnGroundPacket doRead(PacketClient client) throws IOException {
        boolean onGround = client.getDataInputStream().readBoolean();

        return new InputPlayerOnGroundPacket(client, onGround);
    }

}
