package tk.maincraft.entity;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;


import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Vehicle;
import org.bukkit.util.Vector;

import tk.maincraft.MainServer;

public abstract class MainLivingEntity extends MainEntity implements LivingEntity {

    protected int health = this.getMaxHealth();
    protected int remainingAir;
    protected int maximumAir;
    protected Vehicle vehicle = null;
    protected int maxNoDamageTicks = 20;
    protected int noDamageTicks = 0;

    public MainLivingEntity(Location location, Vector velocity, int intId, UUID uniqueId, MainServer server, int maxAir) {
        super(location, velocity, intId, uniqueId, server);
        this.maximumAir = maxAir;
        this.remainingAir = maximumAir;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void setHealth(int health) {
        if ((health < 0) || (health > getMaxHealth())) {
            throw new IllegalArgumentException("Health must be between 0 and " + getMaxHealth());
        }

        this.health = health;
    }

    @Override
    public abstract int getMaxHealth();

    @Override
    public double getEyeHeight() {
        return 1.0D;
    }

    @Override
    public double getEyeHeight(boolean ignoreSneaking) {
        return getEyeHeight();
    }

    @Override
    public Location getEyeLocation() {
        Location loc = getLocation().clone();
        loc.setY(loc.getY() + getEyeHeight());
        return loc;
    }

    @Override
    public List<Block> getLineOfSight(HashSet<Byte> paramHashSet, int paramInt) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Block getTargetBlock(HashSet<Byte> paramHashSet, int paramInt) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Block> getLastTwoTargetBlocks(HashSet<Byte> paramHashSet, int paramInt) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Egg throwEgg() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Snowball throwSnowball() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Arrow shootArrow() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isInsideVehicle() {
        return vehicle != null;
    }

    @Override
    public boolean leaveVehicle() {
        if (!isInsideVehicle())
            return false;

        this.vehicle.eject();
        this.vehicle = null;
        return true;
    }

    @Override
    public Vehicle getVehicle() {
        return vehicle;
    }

    @Override
    public int getRemainingAir() {
        return remainingAir;
    }

    @Override
    public void setRemainingAir(int paramInt) {
        this.remainingAir = paramInt;
    }

    @Override
    public int getMaximumAir() {
        return maximumAir;
    }

    @Override
    public void setMaximumAir(int paramInt) {
        this.maximumAir = paramInt;
    }

    @Override
    public abstract void damage(int paramInt);

    @Override
    public abstract void damage(int paramInt, Entity source);

    @Override
    public int getMaximumNoDamageTicks() {
        return maxNoDamageTicks;
    }

    @Override
    public void setMaximumNoDamageTicks(int paramInt) {
        this.maxNoDamageTicks = paramInt;
    }

    @Override
    public int getLastDamage() {
        return this.getLastDamageCause().getDamage();
    }

    @Override
    public void setLastDamage(int paramInt) {
        this.getLastDamageCause().setDamage(paramInt);
    }

    @Override
    public int getNoDamageTicks() {
        return noDamageTicks;
    }

    @Override
    public void setNoDamageTicks(int paramInt) {
        if (paramInt < 0)
            throw new IllegalArgumentException();

        this.noDamageTicks = paramInt;
    }

    @Override
    public Player getKiller() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public abstract int getMaxFireTicks();

}
