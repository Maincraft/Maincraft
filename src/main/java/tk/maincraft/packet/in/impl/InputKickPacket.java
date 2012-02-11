package tk.maincraft.packet.in.impl;

import tk.maincraft.MainServer;
import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.in.KickPacket;

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
