package tk.maincraft.packet.in.impl;

import tk.maincraft.MainServer;
import tk.maincraft.packet.IllegalPacketDataException;
import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.in.KeepAlivePacket;

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
