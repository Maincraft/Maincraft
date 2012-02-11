package tk.maincraft.packet.in.impl.handlers;

import java.io.IOException;

import tk.maincraft.network.NetUtils;
import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.in.LoginPacket;
import tk.maincraft.packet.in.impl.InputLoginPacket;


public final class LoginPacketHandler extends AbstractPacketHandler<LoginPacket> {
    @Override
    public LoginPacket doRead(PacketClient client) throws IOException {
        int protocolVersion = client.getDataInputStream().readInt();
        String username = NetUtils.readString(client, 16);

        // now just read some dummy slots that the client sends but we don't need
        client.getDataInputStream().readLong(); // mapSeed
        client.getDataInputStream().readInt(); // serverMode
        client.getDataInputStream().readByte(); // worldType
        client.getDataInputStream().readByte(); // difficultySetting
        client.getDataInputStream().readByte(); // worldHeight
        client.getDataInputStream().readByte(); // maxPlayers

        return new InputLoginPacket(client, protocolVersion, username);
    }
}
