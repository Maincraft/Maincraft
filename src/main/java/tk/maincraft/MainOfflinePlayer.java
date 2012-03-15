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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOp() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOp(boolean value) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> serialize() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOnline() {
        for (Player player : Maincraft.getServer().getOnlineMainPlayers()) {
            if (player.getName().equals(getName()))
                return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBanned() {
        return Maincraft.getServer().getBannedUsernames().contains(getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBanned(boolean banned) {
        if (banned) {
            Maincraft.getServer().banPlayerName(getName());
        }
        else {
            Maincraft.getServer().unbanPlayerName(getName());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isWhitelisted() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setWhitelisted(boolean value) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player getPlayer() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getFirstPlayed() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLastPlayed() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasPlayedBefore() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Location getBedSpawnLocation() {
        // TODO Auto-generated method stub
        return null;
    }
}
