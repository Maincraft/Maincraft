package tk.maincraft.packet.out.impl;

import org.bukkit.Bukkit;

import tk.maincraft.MainServer;
import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.impl.AbstractPacket;
import tk.maincraft.packet.out.OutputPacket;


public abstract class AbstractOutputPacket extends AbstractPacket implements OutputPacket {
    public AbstractOutputPacket(PacketClient client) {
        super(client);
    }

    @Override
    public void send() {
        if (this.client == null)
            ((MainServer) Bukkit.getServer()).broadcastPacket(this);
        else
            this.client.send(this);
    }

    @Override
    public void send(boolean autocatch) {
        if (this.client == null)
            ((MainServer) Bukkit.getServer()).broadcastPacket(this);
        else
            this.client.send(this, autocatch);
    }
}
