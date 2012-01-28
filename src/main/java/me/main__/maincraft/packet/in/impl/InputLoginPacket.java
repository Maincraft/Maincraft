package me.main__.maincraft.packet.in.impl;

import me.main__.maincraft.MainServer;
import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.in.LoginPacket;
import me.main__.maincraft.util.Versioning;

public class InputLoginPacket extends AbstractInputPacket implements LoginPacket {

    private final int protocolVersion;
    private final String username;

    public InputLoginPacket(PacketClient client, int protocolVersion, String username) {
        super(client);
        this.protocolVersion = protocolVersion;
        this.username = username;
    }

    @Override
    public void process(MainServer server) {
        // check if protocol versions match
        if (!(getProtocolVersion() == Versioning.getMinecraftProtocolVersion())) {
            String message;
            if (getProtocolVersion() > Versioning.getMinecraftProtocolVersion()) {
                message = "Outdated server! (main() is lazy ;))";
            }
            else {
                message = "Outdated client!";
            }
            client.kick(message);
            return;
        }

        // Forward login to server
        boolean result = server.tryLoginPlayer(getUsername(), client);
        if (!result) {
            client.kick("The server doesn't want you - for whatever reason.");
            return;
        }

        // the answer-login-packet is sent in server.tryLoginPlayer()
    }

    @Override
    public int getProtocolVersion() {
        return protocolVersion;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public int getOpcode() {
        return 1;
    }

    @Override
    public String getToStringDescription() {
        return String.format("protocolVersion=\"%d\",username=\"%s\"", protocolVersion, username);
    }
}
