package tk.maincraft.packet.out;

public interface PlayerPositionAndLookPacket extends OutputPacket {
    double getX();

    double getY();

    double getStance();

    double getZ();

    float getYaw();

    float getPitch();

    boolean isOnGround();
}
