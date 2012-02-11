package tk.maincraft.packet.out;

public interface PlayerListItemPacket extends OutputPacket {
    String getPlayerName();

    boolean getOnlineStatus();

    short getPing();
}
