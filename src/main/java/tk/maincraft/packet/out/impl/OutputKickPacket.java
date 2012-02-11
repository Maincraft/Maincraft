package tk.maincraft.packet.out.impl;

import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.out.KickPacket;

public class OutputKickPacket extends AbstractOutputPacket implements KickPacket {

    private final String reason;

    public OutputKickPacket(PacketClient client, String reason) {
        super(client);
        this.reason = reason;
    }

    @Override
    public int getOpcode() {
        return 255;
    }

    @Override
    public String getReason() {
        return reason;
    }

    @Override
    public String getToStringDescription() {
        return String.format("reason=\"%s\"", reason);
    }
}
