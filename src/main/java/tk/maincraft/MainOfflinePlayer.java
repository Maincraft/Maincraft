package tk.maincraft;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class MainOfflinePlayer implements OfflinePlayer {
    private final String name;

    public MainOfflinePlayer(String name) {
        this.name = name;
    }

    @Override
    public boolean isOp() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setOp(boolean value) {
        // TODO Auto-generated method stub

    }

    @Override
    public Map<String, Object> serialize() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isOnline() {
        for (Player player : Maincraft.getServer().getOnlineMainPlayers()) {
            if (player.getName().equals(getName()))
                return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isBanned() {
        return Maincraft.getServer().getBannedUsernames().contains(getName());
    }

    @Override
    public void setBanned(boolean banned) {
        if (banned) {
            Maincraft.getServer().banPlayerName(getName());
        }
        else {
            Maincraft.getServer().unbanPlayerName(getName());
        }
    }

    @Override
    public boolean isWhitelisted() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setWhitelisted(boolean value) {
        // TODO Auto-generated method stub

    }

    @Override
    public Player getPlayer() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getFirstPlayed() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getLastPlayed() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean hasPlayedBefore() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Location getBedSpawnLocation() {
        // TODO Auto-generated method stub
        return null;
    }
}
