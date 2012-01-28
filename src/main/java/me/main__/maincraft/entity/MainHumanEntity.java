package me.main__.maincraft.entity;

import java.util.Set;
import java.util.UUID;

import me.main__.maincraft.MainServer;
import me.main__.maincraft.inventory.MainPlayerInventory;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public abstract class MainHumanEntity extends MainLivingEntity implements HumanEntity {
    protected final PermissibleBase perm = new PermissibleBase(this);
    protected boolean op = false;
    protected GameMode mode;
    protected PlayerInventory inventory = new MainPlayerInventory();
    protected int sleepTicks = 0;
    protected boolean sleeping;

    public MainHumanEntity(Location location, Vector velocity, int intId, UUID uniqueId, MainServer server, int maxAir) {
        super(location, velocity, intId, uniqueId, server, maxAir);
        this.mode = getServer().getDefaultGameMode();
    }

    @Override
    public boolean isPermissionSet(String name) {
        return perm.isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return this.perm.isPermissionSet(perm);
    }

    @Override
    public boolean hasPermission(String name) {
        return perm.hasPermission(name);
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return this.perm.hasPermission(perm);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return perm.addAttachment(plugin, name, value);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return perm.addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return perm.addAttachment(plugin, name, value, ticks);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return perm.addAttachment(plugin, ticks);
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        perm.removeAttachment(attachment);
    }

    @Override
    public void recalculatePermissions() {
        perm.recalculatePermissions();
    }

    @Override
    public void setOp(boolean value) {
        this.op = value;
        perm.recalculatePermissions();
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return perm.getEffectivePermissions();
    }

    @Override
    public GameMode getGameMode() {
        return mode;
    }

    @Override
    public void setGameMode(GameMode mode) {
        if (mode == null) {
            throw new IllegalArgumentException("Mode cannot be null");
        }

        this.mode = mode;
    }

    @Override
    public boolean isOp() {
        return op;
    }

    @Override
    public PlayerInventory getInventory() {
        return inventory;
    }

    @Override
    public ItemStack getItemInHand() {
        return inventory.getItemInHand();
    }

    @Override
    public void setItemInHand(ItemStack paramItemStack) {
        inventory.setItemInHand(paramItemStack);
    }

    @Override
    public boolean isSleeping() {
        return sleeping;
    }

    @Override
    public int getSleepTicks() {
        return sleepTicks;
    }
}
