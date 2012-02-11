package tk.maincraft.packet.in.impl.handlers;

import java.io.IOException;

import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.in.PlayerLookPacket;
import tk.maincraft.packet.in.impl.InputPlayerLookPacket;


public final class PlayerLookPacketHandler extends AbstractPacketHandler<PlayerLookPacket> {
    @Override
    public PlayerLookPacket doRead(PacketClient client) throws IOException {
        float yaw = client.getDataInputStream().readFloat();
        float pitch = client.getDataInputStream().readFloat();
        boolean onGround = client.getDataInputStream().readBoolean();

        return new InputPlayerLookPacket(client, yaw, pitch, onGround);
    }

}
