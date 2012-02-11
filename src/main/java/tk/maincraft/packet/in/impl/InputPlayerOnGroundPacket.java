package tk.maincraft.packet.in.impl;

import tk.maincraft.MainServer;
import tk.maincraft.entity.MainPlayer;
import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.in.PlayerOnGroundPacket;
import tk.maincraft.world.MainWorld;

public class InputPlayerOnGroundPacket extends AbstractInputPacket implements PlayerOnGroundPacket {

    private final boolean onGround;

    public InputPlayerOnGroundPacket(PacketClient client, boolean onGround) {
        super(client);
        this.onGround = onGround;
    }

    @Override
    public void process(MainServer server) {
        MainPlayer player = client.getPlayer();
        MainWorld world = (MainWorld) player.getWorld();
        world.getEntityManager().handlePlayerOnGround(player, onGround);
    }

    @Override
    public int getOpcode() {
        return 0x0A;
    }

    @Override
    public boolean isOnGround() {
        return onGround;
    }

    @Override
    public String getToStringDescription() {
        return String.format("onGround=\"%b\"", onGround);
    }
}
