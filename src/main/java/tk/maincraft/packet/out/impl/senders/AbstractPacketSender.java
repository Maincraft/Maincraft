package tk.maincraft.packet.out.impl.senders;

import java.io.IOException;

import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.UnexpectedSocketIOException;
import tk.maincraft.packet.out.OutputPacket;
import tk.maincraft.packet.out.PacketSender;


public abstract class AbstractPacketSender<T extends OutputPacket> implements PacketSender<T> {
    @Override
    public void send(PacketClient client, T packet) {
        try {
            client.getDataOutputStream().write(packet.getOpcode());
            sendData(client, packet);
        } catch (IOException e) {
            throw new UnexpectedSocketIOException(String.format(
                    "Error while trying to send packet %1$d (0x0%1$X) to client!",
                    packet.getOpcode()), e);
        }
    }

    public abstract void sendData(PacketClient client, T packet) throws IOException;
}
