package tk.maincraft.packet.out;

public interface EntityLookAndRelMovePacket extends OutputPacket {
    int getEntityId();

    byte getDiffX();

    byte getDiffY();

    byte getDiffZ();

    byte getYaw();

    byte getPitch();
}
