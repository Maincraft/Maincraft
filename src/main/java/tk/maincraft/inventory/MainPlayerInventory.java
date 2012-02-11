package tk.maincraft.inventory;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class MainPlayerInventory extends MainInventory implements PlayerInventory {

    // 36 = 4 rows of 9
    // ... + 4 = armor, completed inventory
    public static final int PLAYER_INVENTORY_SIZE = 40;
    public static final int HELMET_SLOT = 36;
    public static final int CHESTPLATE_SLOT = 37;
    public static final int LEGGINGS_SLOT = 38;
    public static final int BOOTS_SLOT = 39;

    public static final SlotConverter slotConverter = new PlayerInventorySlotConverter();

    private int heldItemSlot = 0;

    public MainPlayerInventory() {
        // all player inventories are ID 0
        super(0, PLAYER_INVENTORY_SIZE);
    }

    @Override
    public ItemStack[] getArmorContents() {
        ItemStack[] armor = new ItemStack[4];

        armor[0] = this.getHelmet();
        armor[1] = this.getChestplate();
        armor[2] = this.getLeggings();
        armor[3] = this.getBoots();

        return armor;
    }

    @Override
    public ItemStack getHelmet() {
        return this.getItem(HELMET_SLOT);
    }

    @Override
    public ItemStack getChestplate() {
        return this.getItem(CHESTPLATE_SLOT);
    }

    @Override
    public ItemStack getLeggings() {
        return this.getItem(LEGGINGS_SLOT);
    }

    @Override
    public ItemStack getBoots() {
        return this.getItem(BOOTS_SLOT);
    }

    @Override
    public void setArmorContents(ItemStack[] newArmor) {
        if (newArmor.length != 4)
            throw new IllegalArgumentException();

        this.setHelmet(newArmor[0]);
        this.setChestplate(newArmor[1]);
        this.setLeggings(newArmor[2]);
        this.setBoots(newArmor[3]);
    }

    @Override
    public void setHelmet(ItemStack paramItemStack) {
        this.setItem(HELMET_SLOT, paramItemStack);
    }

    @Override
    public void setChestplate(ItemStack paramItemStack) {
        this.setItem(CHESTPLATE_SLOT, paramItemStack);
    }

    @Override
    public void setLeggings(ItemStack paramItemStack) {
        this.setItem(LEGGINGS_SLOT, paramItemStack);
    }

    @Override
    public void setBoots(ItemStack paramItemStack) {
        this.setItem(BOOTS_SLOT, paramItemStack);
    }

    @Override
    public ItemStack getItemInHand() {
        return this.getItem(heldItemSlot);
    }

    @Override
    public void setItemInHand(ItemStack paramItemStack) {
        this.setItem(heldItemSlot, paramItemStack);
    }

    @Override
    public int getHeldItemSlot() {
        return heldItemSlot;
    }

    @Override
    public SlotConverter getSlotConverter() {
        return slotConverter;
    }

    @Override
    public String getName() {
        return "Player Inventory";
    }

}
