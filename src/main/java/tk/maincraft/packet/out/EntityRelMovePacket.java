package tk.maincraft.packet.out;

public interface EntityRelMovePacket extends OutputPacket {
    int getEntityId();

    byte getDiffX();

    byte getDiffY();

    byte getDiffZ();
}
