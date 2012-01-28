package me.main__.maincraft.entity;

import java.util.List;
import java.util.UUID;

import me.main__.maincraft.MainServer;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.util.Vector;

public abstract class MainEntity implements Entity {
    protected Location location;
    protected Location oldLocation;
    protected Vector velocity;
    protected MainEntity passenger = null;
    protected int ticksLived = 0;
    protected int fireTicks = 0;
    protected float fallDistance = 0;
    protected EntityDamageEvent lastDamageCause = null;
    protected boolean dead = false;

    protected final int integerId;
    protected final UUID uniqueId;
    protected final MainServer server;

    public MainEntity(Location location, Vector velocity, int intId, UUID uniqueId, MainServer server) {
        this.location = location;
        this.oldLocation = this.location;
        this.velocity = velocity;
        this.integerId = intId;
        this.uniqueId = uniqueId;
        this.server = server;
    }

    public Location getOldLocation() {
        return oldLocation;
    }

    public void setOldLocation(Location oldLocation) {
        this.oldLocation = oldLocation;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void setVelocity(Vector paramVector) {
        if (paramVector == null)
            throw new IllegalArgumentException();

        this.velocity = paramVector;
    }

    @Override
    public Vector getVelocity() {
        return velocity;
    }

    @Override
    public World getWorld() {
        return location.getWorld();
    }

    @Override
    public boolean teleport(Location paramLocation) {
        return teleport(paramLocation, TeleportCause.PLUGIN);
    }

    @Override
    public boolean teleport(Location paramLocation, TeleportCause paramTeleportCause) {
        // TODO maybe event?
        this.location = paramLocation;
        return false;
    }

    @Override
    public boolean teleport(Entity paramEntity) {
        return teleport(paramEntity, TeleportCause.PLUGIN);
    }

    @Override
    public boolean teleport(Entity paramEntity, TeleportCause paramTeleportCause) {
        return teleport(paramEntity.getLocation(), paramTeleportCause);
    }

    @Override
    public List<Entity> getNearbyEntities(double paramDouble1, double paramDouble2,
            double paramDouble3) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getEntityId() {
        return integerId;
    }

    @Override
    public int getFireTicks() {
        return fireTicks;
    }

    @Override
    public abstract int getMaxFireTicks();

    @Override
    public void setFireTicks(int paramInt) {
        if (paramInt < 0)
            throw new IllegalArgumentException();

        this.fireTicks = paramInt;
    }

    @Override
    public void remove() {
        dead = true;
    }

    @Override
    public boolean isDead() {
        return dead;
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public Entity getPassenger() {
        return passenger;
    }

    @Override
    public boolean setPassenger(Entity paramEntity) {
        if (paramEntity instanceof MainEntity) {
            this.passenger = (MainEntity) paramEntity;
            return true;
        }
        else
            return false;
    }

    @Override
    public boolean isEmpty() {
        return passenger == null;
    }

    @Override
    public boolean eject() {
        if (isEmpty())
            return false;
        else {
            passenger = null;
            return true;
        }
    }

    @Override
    public float getFallDistance() {
        return fallDistance;
    }

    @Override
    public void setFallDistance(float paramFloat) {
        if (paramFloat < 0)
            throw new IllegalArgumentException();

        this.fallDistance = paramFloat;
    }

    @Override
    public void setLastDamageCause(EntityDamageEvent paramEntityDamageEvent) {
        this.lastDamageCause = paramEntityDamageEvent;
    }

    @Override
    public EntityDamageEvent getLastDamageCause() {
        return lastDamageCause;
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public int getTicksLived() {
        return ticksLived;
    }

    @Override
    public void setTicksLived(int paramInt) {
        if (paramInt < 0)
            throw new IllegalArgumentException();

        this.ticksLived = paramInt;
    }

}
