package me.main__.maincraft.packet.in.impl.handlers;

import java.io.IOException;

import me.main__.maincraft.packet.IllegalPacketDataException;
import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.in.InputPacket;
import me.main__.maincraft.packet.in.PacketHandler;

public abstract class AbstractPacketHandler<T extends InputPacket> implements PacketHandler<T> {
    @Override
    public T handle(PacketClient client) {
        try {
            return doRead(client);
        } catch (IOException e) {
            throw new IllegalPacketDataException(
                    "An exception occured while trying to read a packet!", e);
        }
    }

    public abstract T doRead(PacketClient client) throws IOException;

}
