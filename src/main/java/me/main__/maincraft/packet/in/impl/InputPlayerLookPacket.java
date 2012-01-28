package me.main__.maincraft.packet.in.impl;

import me.main__.maincraft.MainServer;
import me.main__.maincraft.entity.MainPlayer;
import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.in.PlayerLookPacket;
import me.main__.maincraft.world.MainWorld;

public class InputPlayerLookPacket extends AbstractInputPacket implements PlayerLookPacket {

    private final float yaw;
    private final float pitch;
    private final boolean onGround;

    public InputPlayerLookPacket(PacketClient client, float yaw, float pitch, boolean onGround) {
        super(client);
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    @Override
    public void process(MainServer server) {
        MainPlayer player = client.getPlayer();
        MainWorld world = (MainWorld) player.getWorld();
        world.getEntityManager().handlePlayerLook(player, pitch, yaw);
        world.getEntityManager().handlePlayerOnGround(player, onGround);
    }

    @Override
    public int getOpcode() {
        return 0x0C;
    }

    @Override
    public float getYaw() {
        return yaw;
    }

    @Override
    public float getPitch() {
        return pitch;
    }

    @Override
    public boolean isOnGround() {
        return onGround;
    }

    @Override
    public String getToStringDescription() {
        return String.format("yaw=\"%f\",pitch=\"%f\",onGround=\"%b\"", yaw, pitch, onGround);
    }

}
