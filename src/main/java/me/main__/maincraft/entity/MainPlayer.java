package me.main__.maincraft.entity;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;
import java.util.UUID;

import me.main__.maincraft.EventFactory;
import me.main__.maincraft.MainServer;
import me.main__.maincraft.network.NetworkClient;
import me.main__.maincraft.network.PacketClientUsing;
import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.world.ClientView;

import org.bukkit.Achievement;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Statistic;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.map.MapView;
import org.bukkit.util.Vector;

public class MainPlayer extends MainHumanEntity implements Player, PacketClientUsing {
    private final String name;
    private String displayName;
    private String playerListName;
    private PacketClient packetClient;
    private Location compassTarget = new Location(getWorld(), 0, 0, 0);
    private final ClientView clientView = new ClientView(this);
    private boolean sneaking = false;
    private boolean sprinting = false;
    private boolean sleepingIgnored;

    public MainPlayer(Location location, Vector velocity, int intId, UUID uniqueId, MainServer server, String name, PacketClient client) {
        super(location, velocity, intId, uniqueId, server, 300);
        this.name = name;
        this.displayName = name;
        this.playerListName = name;
        this.packetClient = client;
    }

    public ClientView getClientView() {
        return clientView;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void sendMessage(String message) {
        getPacketClient().chat(message);
    }

    @Override
    public boolean isOnline() {
        for (Player p : server.getOnlinePlayers()) {
            if (p == this)
                return true;
        }
        return false;
    }

    @Override
    public boolean isBanned() {
        return server.getBannedUsernames().contains(getName());
    }

    @Override
    public void setBanned(boolean paramBoolean) {
        server.banPlayerName(getName());
    }

    @Override
    public boolean isWhitelisted() {
        return server.getWhitelistedUsernames().contains(getName());
    }

    @Override
    public void setWhitelisted(boolean paramBoolean) {
        server.whitelistPlayerName(getName());
    }

    @Override
    public Player getPlayer() {
        return this;
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
    public Map<String, Object> serialize() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public void setDisplayName(String paramString) {
        this.displayName = paramString;
    }

    @Override
    public String getPlayerListName() {
        return this.playerListName;
    }

    @Override
    public void setPlayerListName(String paramString) {
        this.playerListName = paramString;
    }

    @Override
    public void setCompassTarget(Location paramLocation) {
        this.compassTarget = paramLocation;
    }

    @Override
    public Location getCompassTarget() {
        return compassTarget;
    }

    @Override
    public InetSocketAddress getAddress() {
        if (packetClient instanceof NetworkClient) {
            NetworkClient nc = (NetworkClient) packetClient;
            SocketAddress sa = nc.getSocket().getRemoteSocketAddress();
            if (sa instanceof InetSocketAddress) {
                return (InetSocketAddress) sa;
            }
        }
        return null;
    }

    @Override
    public void sendRawMessage(String message) {
        this.sendMessage(message);
    }

    @Override
    public void kickPlayer(String reason) {
        packetClient.kick(reason);
    }

    @Override
    public void chat(String s) {
        if (!this.isDead()) {
            if (s.startsWith("/")) {
                PlayerCommandPreprocessEvent event = EventFactory
                        .onPlayerCommandPreprocess(this, s);
                if (event.isCancelled())
                    return;
                s = event.getMessage();

                getServer().dispatchCommand(this, s.substring(1));
            }
            else {
                PlayerChatEvent event = EventFactory.onPlayerChat(this, s);
                if (event.isCancelled())
                    return;

                s = String.format(event.getFormat(), event.getPlayer().getDisplayName(),
                        event.getMessage());
                Bukkit.getLogger().info(s);
                for (Player recipient : event.getRecipients()) {
                    recipient.sendMessage(s);
                }
            }
        }
    }

    @Override
    public boolean performCommand(String paramString) {
        return this.getServer().dispatchCommand(this, paramString);
    }

    @Override
    public boolean isSneaking() {
        return sneaking;
    }

    @Override
    public void setSneaking(boolean paramBoolean) {
        this.sneaking = paramBoolean;
    }

    @Override
    public boolean isSprinting() {
        return sprinting;
    }

    @Override
    public void setSprinting(boolean paramBoolean) {
        this.sprinting = paramBoolean;
    }

    @Override
    public void saveData() {
        // TODO Auto-generated method stub

    }

    @Override
    public void loadData() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setSleepingIgnored(boolean paramBoolean) {
        this.sleepingIgnored = paramBoolean;
    }

    @Override
    public boolean isSleepingIgnored() {
        return sleepingIgnored;
    }

    @Override
    public void playNote(Location paramLocation, byte paramByte1, byte paramByte2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void playNote(Location paramLocation, Instrument paramInstrument, Note paramNote) {
        // TODO Auto-generated method stub

    }

    @Override
    public void playEffect(Location paramLocation, Effect paramEffect, int paramInt) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendBlockChange(Location location, Material material, byte data) {
        sendBlockChange(location, material.getId(), data);
    }

    @Override
    public void sendBlockChange(Location paramLocation, int paramInt, byte paramByte) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean sendChunkChange(Location paramLocation, int paramInt1, int paramInt2,
            int paramInt3, byte[] paramArrayOfByte) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void sendMap(MapView paramMapView) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateInventory() {
        // TODO Auto-generated method stub

    }

    @Override
    public void awardAchievement(Achievement paramAchievement) {
        // TODO Auto-generated method stub

    }

    @Override
    public void incrementStatistic(Statistic paramStatistic) {
        // TODO Auto-generated method stub

    }

    @Override
    public void incrementStatistic(Statistic paramStatistic, int paramInt) {
        // TODO Auto-generated method stub

    }

    @Override
    public void incrementStatistic(Statistic paramStatistic, Material paramMaterial) {
        // TODO Auto-generated method stub

    }

    @Override
    public void incrementStatistic(Statistic paramStatistic, Material paramMaterial, int paramInt) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPlayerTime(long paramLong, boolean paramBoolean) {
        // TODO Auto-generated method stub

    }

    @Override
    public long getPlayerTime() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getPlayerTimeOffset() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isPlayerTimeRelative() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void resetPlayerTime() {
        // TODO Auto-generated method stub

    }

    @Override
    public void giveExp(int paramInt) {
        // TODO Auto-generated method stub

    }

    @Override
    public float getExp() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setExp(float paramFloat) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getExperience() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setExperience(int paramInt) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getLevel() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setLevel(int paramInt) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getTotalExperience() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setTotalExperience(int paramInt) {
        // TODO Auto-generated method stub

    }

    @Override
    public float getExhaustion() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setExhaustion(float paramFloat) {
        // TODO Auto-generated method stub

    }

    @Override
    public float getSaturation() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setSaturation(float paramFloat) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getFoodLevel() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setFoodLevel(int paramInt) {
        // TODO Auto-generated method stub

    }

    @Override
    public Location getBedSpawnLocation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getMaxHealth() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void damage(int paramInt) {
        // TODO Auto-generated method stub

    }

    @Override
    public void damage(int paramInt, Entity source) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getMaxFireTicks() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public PacketClient getPacketClient() {
        return packetClient;
    }

    @Override
    public void setPacketClient(PacketClient pc) {
        if (pc == null)
            throw new IllegalArgumentException();

        this.packetClient = pc;
    }

}
