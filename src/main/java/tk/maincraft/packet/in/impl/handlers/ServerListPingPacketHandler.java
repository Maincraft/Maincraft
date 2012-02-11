package tk.maincraft.packet.in.impl.handlers;

import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.in.PacketHandler;
import tk.maincraft.packet.in.ServerListPing;
import tk.maincraft.packet.in.impl.InputServerListPingPacket;

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
