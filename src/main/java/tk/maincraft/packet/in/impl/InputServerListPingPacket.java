package tk.maincraft.packet.in.impl;

import org.bukkit.event.server.ServerListPingEvent;

import tk.maincraft.MainServer;
import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.in.ServerListPing;


public class InputServerListPingPacket extends AbstractInputPacket implements ServerListPing {

    public InputServerListPingPacket(PacketClient client) {
        super(client);
    }

    @Override
    public void process(MainServer server) {
        ServerListPingEvent event = new ServerListPingEvent(client.getIp(), server.getMOTD(),
                server.getOnlinePlayers().length, server.getMaxPlayers());
        server.getPluginManager().callEvent(event);

        StringBuilder message = new StringBuilder();
        message.append(event.getMotd()).append("\247");
        message.append(event.getNumPlayers()).append("\247");
        message.append(event.getMaxPlayers());

        client.kick(message.toString());
    }

    @Override
    public int getOpcode() {
        return 254;
    }

    @Override
    public String getToStringDescription() {
        return new String();
    }
}
