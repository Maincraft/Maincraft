package me.main__.maincraft.inventory;

import org.bukkit.inventory.ItemStack;

public interface InventoryViewer {
    void onSlotSet(MainInventory mainInventory, int index, ItemStack itemStack);
}
