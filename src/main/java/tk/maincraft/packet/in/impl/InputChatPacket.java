package tk.maincraft.packet.in.impl;

import tk.maincraft.MainServer;
import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.in.ChatPacket;

public class InputChatPacket extends AbstractInputPacket implements ChatPacket {

    private final String message;

    public InputChatPacket(PacketClient client, String message) {
        super(client);
        this.message = message;
    }

    @Override
    public int getOpcode() {
        return 3;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void process(MainServer server) {
        // TODO Auto-generated method stub
        client.getPlayer().chat(message);
        // server.broadcastMessage(message);
        // throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public String getToStringDescription() {
        return String.format("message=\"%s\"", message);
    }
}
