package me.main__.maincraft.packet.in.impl;

import me.main__.maincraft.MainServer;
import me.main__.maincraft.packet.IllegalPacketDataException;
import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.in.KeepAlivePacket;

public class InputKeepAlivePacket extends AbstractInputPacket implements KeepAlivePacket {

    private final int id;

    public InputKeepAlivePacket(PacketClient client, int id) {
        super(client);
        this.id = id;
    }

    @Override
    public void process(MainServer server) {
        if (id == 0)
            return; // the client just sent this because he wanted to

        if (!getClient().handleKeepAlive(getToken())) {
            throw new IllegalPacketDataException(
                    "The client sent a KeepAlive-packet with INVALID contents!");
        }
    }

    @Override
    public int getOpcode() {
        return 0;
    }

    @Override
    public int getToken() {
        return id;
    }

    @Override
    public String getToStringDescription() {
        return String.format("id=\"%d\"", id);
    }
}
