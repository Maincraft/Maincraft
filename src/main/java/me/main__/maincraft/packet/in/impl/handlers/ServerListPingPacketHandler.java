package me.main__.maincraft.packet.in.impl.handlers;

import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.in.PacketHandler;
import me.main__.maincraft.packet.in.ServerListPing;
import me.main__.maincraft.packet.in.impl.InputServerListPingPacket;

/*
 * All PacketHandlers extend AbstractPacketHandler, only this one is an exception:
 * Since it actually doesn't read anything, we don't need the AbstractPacketHandler around it.
 */
public final class ServerListPingPacketHandler implements PacketHandler<ServerListPing> {
    @Override
    public ServerListPing handle(PacketClient client) {
        return new InputServerListPingPacket(client);
    }
}
