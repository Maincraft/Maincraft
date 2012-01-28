package me.main__.maincraft.inventory;

public interface SlotConverter {
    int netToLocal(int net);

    int localToNet(int local);
}
