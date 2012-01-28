package me.main__.maincraft.network;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import me.main__.maincraft.packet.PacketNotFoundException;
import me.main__.maincraft.packet.out.OutputPacket;
import me.main__.maincraft.packet.out.PacketSender;
import me.main__.maincraft.packet.out.impl.senders.*;

public final class PacketSenderFactory {
    private PacketSenderFactory() {
    }

    private static final Map<Integer, Class<? extends PacketSender<? extends OutputPacket>>> classMappings = new HashMap<Integer, Class<? extends PacketSender<? extends OutputPacket>>>();
    private static final Map<Integer, PacketSender<? extends OutputPacket>> objMappings = new HashMap<Integer, PacketSender<? extends OutputPacket>>();

    static {
        registerSender(0x00, KeepAlivePacketSender.class);
        registerSender(0x01, LoginPacketSender.class);
        registerSender(0x02, HandshakePacketSender.class);
        registerSender(0x03, ChatPacketSender.class);
        registerSender(0x06, SpawnPositionPacketSender.class);
        registerSender(0x0D, PlayerPositionAndLookPacketSender.class);
        registerSender(0x14, NamedEntitySpawnPacketSender.class);
        registerSender(0x1D, DestroyEntityPacketSender.class);
        registerSender(0x1F, EntityRelMovePacketSender.class);
        registerSender(0x20, EntityLookPacketSender.class);
        registerSender(0x21, EntityLookAndRelMovePacketSender.class);
        registerSender(0x22, EntityTeleportPacketSender.class);
        registerSender(0x32, PreChunkPacketSender.class);
        registerSender(0x33, MapChunkPacketSender.class);
        registerSender(0xC9, PlayerListItemPacketSender.class);
        registerSender(0xFF, KickPacketSender.class);
    }

    public static PacketSender<?> getNewPacketSender(int opcode) throws PacketNotFoundException {
        try {
            if (objMappings.containsKey(opcode))
                return objMappings.get(opcode);
            else {
                Class<? extends PacketSender<? extends OutputPacket>> clazz = classMappings
                        .get(opcode);
                Constructor<? extends PacketSender<? extends OutputPacket>> ctor = clazz
                        .getConstructor();
                PacketSender<? extends OutputPacket> newSender = ctor.newInstance();
                objMappings.put(opcode, newSender);
                return newSender;
            }
        } catch (Throwable t) {
            throw new PacketNotFoundException(String.format(
                    "Failed to get PacketSender for %1$d (0x0%1$X)", opcode), t);
        }
    }

    public static void registerSender(int opcode,
            Class<? extends PacketSender<? extends OutputPacket>> sender) {
        classMappings.put(opcode, sender);
    }
}
