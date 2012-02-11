package tk.maincraft.packet.in.impl;

import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.impl.AbstractPacket;
import tk.maincraft.packet.in.InputPacket;

public abstract class AbstractInputPacket extends AbstractPacket implements InputPacket {

    public AbstractInputPacket(PacketClient client) {
        super(client);
        // TODO Auto-generated constructor stub
    }

}
