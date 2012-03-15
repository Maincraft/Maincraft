package tk.maincraft.network;

public interface PacketClientAction<T> {
    public T call(PacketClient client) throws Exception;
}
