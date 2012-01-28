package me.main__.maincraft.packet.out.impl;

import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.out.PlayerListItemPacket;

public class OutputPlayerListItemPacket extends AbstractOutputPacket implements
        PlayerListItemPacket {
    private final String playerName;
    private final boolean onlineStatus;
    private final short ping;

    public OutputPlayerListItemPacket(PacketClient client, String playerName, boolean onlineStatus, short ping) {
        super(client);
        this.playerName = playerName;
        this.onlineStatus = onlineStatus;
        this.ping = ping;
    }

    @Override
    public int getOpcode() {
        return 0xC9;
    }

    @Override
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public boolean getOnlineStatus() {
        return onlineStatus;
    }

    @Override
    public short getPing() {
        return ping;
    }

    @Override
    public String getToStringDescription() {
        return String.format("playerName=\"%s\",onlineStatus=\"%b\",ping=\"%d\"", playerName,
                onlineStatus, ping);
    }

}
