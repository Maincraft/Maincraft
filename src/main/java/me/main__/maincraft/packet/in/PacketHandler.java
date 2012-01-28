package me.main__.maincraft.packet.in;

import me.main__.maincraft.packet.PacketClient;

/**
 * A {@link PacketHandler} reads packet-data and creates an {@link InputPacket} from that data.
 * <p>
 * <b>IMPORTANT: A {@link PacketHandler} is usually only created once and then reused, so <i>it must not remain in an unusable state</i>!</b>
 * 
 * @param <T> The type of {@link InputPacket} constructed by this {@link PacketHandler}.
 */
public interface PacketHandler<T extends InputPacket> {
    T handle(PacketClient client);
}
