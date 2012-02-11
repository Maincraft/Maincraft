package tk.maincraft.network;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import tk.maincraft.packet.Packet;
import tk.maincraft.packet.PacketNotFoundException;
import tk.maincraft.packet.in.PacketHandler;
import tk.maincraft.packet.in.impl.handlers.*;


public final class PacketHandlerFactory {
    private PacketHandlerFactory() {
    }

    private static final Map<Integer, Class<? extends PacketHandler<?>>> classMappings = new HashMap<Integer, Class<? extends PacketHandler<?>>>();
    private static final Map<Integer, PacketHandler<?>> objMappings = new HashMap<Integer, PacketHandler<?>>();

    static {
        registerHandler(0x00, KeepAlivePacketHandler.class);
        registerHandler(0x01, LoginPacketHandler.class);
        registerHandler(0x02, HandshakePacketHandler.class);
        registerHandler(0x03, ChatPacketHandler.class);
        registerHandler(0x0A, PlayerOnGroundPacketHandler.class);
        registerHandler(0x0B, PlayerPositionPacketHandler.class);
        registerHandler(0x0C, PlayerLookPacketHandler.class);
        registerHandler(0x0D, PlayerPositionAndLookPacketHandler.class);
        registerHandler(0xFE, ServerListPingPacketHandler.class);
        registerHandler(0xFF, KickPacketHandler.class);
    }

    public static PacketHandler<?> getPacketHandler(int opcode) throws PacketNotFoundException {
        try {
            if (objMappings.containsKey(opcode))
                return objMappings.get(opcode);
            else {
                Class<? extends PacketHandler<? extends Packet>> clazz = classMappings.get(opcode);
                Constructor<? extends PacketHandler<? extends Packet>> ctor = clazz
                        .getConstructor();
                PacketHandler<?> newHandler = ctor.newInstance();
                objMappings.put(opcode, newHandler);
                return newHandler;
            }
        } catch (Throwable t) {
            throw new PacketNotFoundException(String.format(
                    "Failed to get PacketHandler for %1$d (0x0%1$X)", opcode), t);
        }
    }

    public static void registerHandler(int opcode, Class<? extends PacketHandler<?>> handler) {
        classMappings.put(opcode, handler);
    }

}
