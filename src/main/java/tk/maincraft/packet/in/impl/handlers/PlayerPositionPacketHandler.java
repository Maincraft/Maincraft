package tk.maincraft.packet.in.impl.handlers;

import java.io.IOException;

import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.in.PlayerPositionPacket;
import tk.maincraft.packet.in.impl.InputPlayerPositionPacket;


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
