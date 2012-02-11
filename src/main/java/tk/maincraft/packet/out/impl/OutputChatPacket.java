package tk.maincraft.packet.out.impl;

import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.out.ChatPacket;

public class OutputChatPacket extends AbstractOutputPacket implements ChatPacket {

    private final String message;

    public OutputChatPacket(PacketClient client, String message) {
        super(client);
        this.message = message;
    }

    @Override
    public int getOpcode() {
        return 3;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getToStringDescription() {
        return String.format("message=\"%s\"", message);
    }
}
