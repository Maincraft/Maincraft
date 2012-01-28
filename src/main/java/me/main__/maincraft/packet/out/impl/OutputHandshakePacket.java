package me.main__.maincraft.packet.out.impl;

import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.out.HandshakePacket;

public class OutputHandshakePacket extends AbstractOutputPacket implements HandshakePacket {
    private final String message;

    public OutputHandshakePacket(PacketClient client, String message) {
        super(client);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getOpcode() {
        return 2;
    }

    @Override
    public String getToStringDescription() {
        return String.format("message=\"%s\"", message);
    }
}
