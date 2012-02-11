package tk.maincraft.network;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.bukkit.Bukkit;

import tk.maincraft.Maincraft;
import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.PacketNotFoundException;
import tk.maincraft.packet.in.InputPacket;
import tk.maincraft.packet.in.PacketHandler;


public final class PacketReader {
    private PacketReader() {
    }

    /*
     * And this is how it works: First, we read the packet ID. There are several PacketHandlers registered in the PacketHandlerFactory and the factory tries to create one for us. We call the PacketHandler's handle-method and he reads the packet. We can then return it.
     */

    public static InputPacket read(PacketClient client) throws IOException, PacketNotFoundException {
        int opcode;
        try {
            opcode = client.getDataInputStream().read();
        } catch (SocketTimeoutException e) {
            client.keepAlive();
            return null;
        }

        if (opcode == -1) {
            // disconnected...?
            return null;
        }

        Bukkit.getLogger().finer(String.format("Recv %1$d (0x0%1$X)", opcode));

        PacketHandler<?> packetHandler = PacketHandlerFactory.getPacketHandler(opcode);
        InputPacket packet = null;
        if (packetHandler != null)
            packet = packetHandler.handle(client);

        if (Maincraft.getServer().getConfig().getDebugSettings().getDetailedPacketDebug())
            Bukkit.getLogger().finest(
                    String.format("Got a %s from %s",
                            ((packet == null) ? "NULL-packet!" : packet.toString()),
                            client.getIpString()));

        return packet;
    }
}
