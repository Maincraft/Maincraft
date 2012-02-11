package tk.maincraft.packet.in.impl;

import tk.maincraft.MainServer;
import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.in.HandshakePacket;
import tk.maincraft.packet.out.impl.OutputHandshakePacket;

public class InputHandshakePacket extends AbstractInputPacket implements HandshakePacket {

    private final String username;

    public InputHandshakePacket(PacketClient client, String username) {
        super(client);
        this.username = username;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void process(MainServer server) {
        // the client wants a handshake? that's what we do now!
        OutputHandshakePacket outPacket = new OutputHandshakePacket(client, "-"); // TODO auth
        outPacket.send();
    }

    @Override
    public int getOpcode() {
        return 2;
    }

    @Override
    public String getToStringDescription() {
        return String.format("username=\"%s\"", username);
    }
}
