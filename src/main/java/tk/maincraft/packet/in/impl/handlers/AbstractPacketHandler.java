package tk.maincraft.packet.in.impl.handlers;

import java.io.IOException;

import tk.maincraft.packet.IllegalPacketDataException;
import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.in.InputPacket;
import tk.maincraft.packet.in.PacketHandler;


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
