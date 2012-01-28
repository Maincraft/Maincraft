package me.main__.maincraft.packet;

public interface PacketClientAction<T> {
    public T call(PacketClient client) throws Exception;
}
