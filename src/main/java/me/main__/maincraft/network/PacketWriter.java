package me.main__.maincraft.network;

import java.io.File;
import java.io.FileOutputStream;

import me.main__.maincraft.Maincraft;
import me.main__.maincraft.packet.Packet;
import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.PacketNotFoundException;
import me.main__.maincraft.packet.UnexpectedSocketIOException;
import me.main__.maincraft.packet.out.OutputPacket;
import me.main__.maincraft.packet.out.PacketSender;

import org.bukkit.Bukkit;

public final class PacketWriter {
    private PacketWriter() {
    }

    public static void writePacket(PacketClient client, OutputPacket packet)
            throws PacketNotFoundException, UnexpectedSocketIOException {

        Bukkit.getLogger().finer(String.format("Send %1$d (0x0%1$X)", packet.getOpcode()));

        if (Maincraft.getServer().getConfig().getDebugSettings().getDetailedPacketDebug())
            Bukkit.getLogger().finest(
                    String.format("Sending a %s to %s",
                            ((packet == null) ? "NULL-packet!" : packet.toString()),
                            client.getIpString()));

        if (Maincraft.getServer().getConfig().getDebugSettings().getDumpPackets()) {
            // dump packet
            try {
                PacketClient debugClient = new FileDebugClient(new FileOutputStream(new File(
                        "packet.dmp", packet.getClass().getName() + "@" + packet.hashCode()
                                + ".packetdmp")));
                debugClient.send(packet);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        PacketSender<? extends Packet> genericSender = PacketSenderFactory
                .getNewPacketSender(packet.getOpcode());
        @SuppressWarnings("unchecked")
        PacketSender<Packet> sender = (PacketSender<Packet>) genericSender;
        sender.send(client, packet);
    }
}
