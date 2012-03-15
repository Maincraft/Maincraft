package tk.maincraft.command;

import java.util.Set;


import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import tk.maincraft.MainServer;

public class MainConsoleCommandSender implements ConsoleCommandSender {
    private final MainServer server;
    private final PermissibleBase permBase = new PermissibleBase(this);

    public MainConsoleCommandSender(MainServer server) {
        this.server = server;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(String message) {
        server.getLogger().info(ChatColor.stripColor(message));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Server getServer() {
        return server;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "CONSOLE";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPermissionSet(String name) {
        return permBase.isPermissionSet(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPermissionSet(Permission perm) {
        return this.permBase.isPermissionSet(perm);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasPermission(String name) {
        return permBase.hasPermission(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasPermission(Permission perm) {
        return this.permBase.hasPermission(perm);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return permBase.addAttachment(plugin, name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return permBase.addAttachment(plugin);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return permBase.addAttachment(plugin, name, value, ticks);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return permBase.addAttachment(plugin, ticks);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        permBase.removeAttachment(attachment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void recalculatePermissions() {
        permBase.recalculatePermissions();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return permBase.getEffectivePermissions();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOp() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOp(boolean paramBoolean) {
        throw new UnsupportedOperationException("Can't set operator status of console!");
    }
}
