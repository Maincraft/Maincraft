package me.main__.maincraft;

import java.net.InetAddress;

import me.main__.maincraft.entity.MainPlayer;

import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;

public class EventFactory {
    public static PlayerPreLoginEvent onPlayerPreLogin(String playername, InetAddress address) {
        PlayerPreLoginEvent pple = new PlayerPreLoginEvent(playername, address);
        Bukkit.getPluginManager().callEvent(pple);
        return pple;
    }

    public static PlayerCommandPreprocessEvent onPlayerCommandPreprocess(MainPlayer player,
            String command) {
        PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(player, command);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PlayerChatEvent onPlayerChat(MainPlayer player, String message) {
        PlayerChatEvent event = new PlayerChatEvent(player, message);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }
}
