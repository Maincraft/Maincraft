package me.main__.maincraft.packet.in.impl;

import me.main__.maincraft.MainServer;
import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.in.KickPacket;

public class InputKickPacket extends AbstractInputPacket implements KickPacket {
    private final String reason;

    public InputKickPacket(PacketClient client, String reason) {
        super(client);
        this.reason = reason;
    }

    @Override
    public int getOpcode() {
        return 0xFF;
    }

    @Override
    public String getReason() {
        return reason;
    }

    @Override
    public void process(MainServer server) {
        server.getLogger().info(
                String.format("Player '%s' disconnected: %s", client.getPlayer().getName(),
                        getReason()));
        getClient().disconnect();
    }

    @Override
    public String getToStringDescription() {
        return String.format("reason=\"%s\"", reason);
    }

}
