package me.main__.maincraft;

import java.util.Map;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class MainOfflinePlayer implements OfflinePlayer {

    private final String name;

    public MainOfflinePlayer(String name) {
        this.name = name;
    }

    public boolean isOp() {
        // TODO Auto-generated method stub
        return false;
    }

    public void setOp(boolean value) {
        // TODO Auto-generated method stub

    }

    public Map<String, Object> serialize() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isOnline() {
        for (Player player : Maincraft.getServer().getOnlineMainPlayers()) {
            if (player.getName().equals(getName()))
                return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public boolean isBanned() {
        return Maincraft.getServer().getBannedUsernames().contains(getName());
    }

    public void setBanned(boolean banned) {
        if (banned) {
            Maincraft.getServer().banPlayerName(getName());
        }
        else {
            Maincraft.getServer().unbanPlayerName(getName());
        }
    }

    public boolean isWhitelisted() {
        // TODO Auto-generated method stub
        return false;
    }

    public void setWhitelisted(boolean value) {
        // TODO Auto-generated method stub

    }

    public Player getPlayer() {
        // TODO Auto-generated method stub
        return null;
    }

    public long getFirstPlayed() {
        // TODO Auto-generated method stub
        return 0;
    }

    public long getLastPlayed() {
        // TODO Auto-generated method stub
        return 0;
    }

    public boolean hasPlayedBefore() {
        // TODO Auto-generated method stub
        return false;
    }

}
