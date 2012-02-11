package tk.maincraft.packet.out;

public interface EntityLookPacket extends OutputPacket {
    int getEntityId();

    byte getYaw();

    byte getPitch();
}
