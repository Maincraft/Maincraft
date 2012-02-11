package tk.maincraft.inventory;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class MainInventory implements Inventory {

    private final int id;

    protected final ArrayList<InventoryViewer> viewers = new ArrayList<InventoryViewer>();

    private final ItemStack[] slots;

    protected MainInventory(int id, int size) {
        this.id = id;
        slots = new ItemStack[size];
    }

    public void addViewer(InventoryViewer viewer) {
        viewers.add(viewer);
    }

    public void removeViewer(InventoryViewer viewer) {
        viewers.remove(viewer);
    }

    public abstract SlotConverter getSlotConverter();

    public int getId() {
        return id;
    }

    @Override
    public int getSize() {
        return slots.length;
    }

    @Override
    public abstract String getName();

    private void sendUpdate(int index) {
        for (InventoryViewer viewer : viewers) {
            viewer.onSlotSet(this, index, slots[index]);
        }
    }

    @Override
    public ItemStack getItem(int index) {
        return slots[index];
    }

    @Override
    public void setItem(int index, ItemStack item) {
        slots[index] = item;
        sendUpdate(index);
    }

    @Override
    public HashMap<Integer, ItemStack> addItem(ItemStack... items) {
        HashMap<Integer, ItemStack> result = new HashMap<Integer, ItemStack>();

        for (int i = 0; i < items.length; ++i) {
            int maxStackSize = items[i].getType() == null ? 64 : items[i].getType()
                    .getMaxStackSize();
            int mat = items[i].getTypeId();
            int toAdd = items[i].getAmount();
            short damage = items[i].getDurability();

            for (int j = 0; toAdd > 0 && j < getSize(); ++j) {
                if (slots[j] != null && slots[j].getTypeId() == mat
                        && slots[j].getDurability() == damage) {
                    int space = maxStackSize - slots[j].getAmount();
                    if (space < 0)
                        continue;
                    if (space > toAdd)
                        space = toAdd;

                    slots[j].setAmount(slots[j].getAmount() + space);
                    toAdd -= space;
                    sendUpdate(j);
                }
            }

            if (toAdd > 0) {
                for (int j = 0; toAdd > 0 && j < getSize(); ++j) {
                    if (slots[j] == null) {
                        int num = toAdd > maxStackSize ? maxStackSize : toAdd;
                        slots[j] = new ItemStack(mat, num, damage);
                        toAdd -= num;
                        sendUpdate(j);
                    }
                }
            }

            if (toAdd > 0) {
                result.put(i, new ItemStack(mat, toAdd, damage));
            }
        }

        return result;
    }

    @Override
    public HashMap<Integer, ItemStack> removeItem(ItemStack... items) {
        HashMap<Integer, ItemStack> result = new HashMap<Integer, ItemStack>();

        for (int i = 0; i < items.length; ++i) {
            int mat = items[i].getTypeId();
            int toRemove = items[i].getAmount();
            short damage = items[i].getDurability();

            for (int j = 0; j < getSize(); ++j) {
                if (slots[j] != null && slots[j].getTypeId() == mat
                        && slots[j].getDurability() == damage) {
                    if (slots[j].getAmount() > toRemove) {
                        slots[j].setAmount(slots[j].getAmount() - toRemove);
                    }
                    else {
                        toRemove -= slots[j].getAmount();
                        slots[j] = null;
                    }
                    sendUpdate(j);
                }
            }

            if (toRemove > 0) {
                result.put(i, new ItemStack(mat, toRemove, damage));
            }
        }

        return result;
    }

    @Override
    public ItemStack[] getContents() {
        return slots.clone();
    }

    @Override
    public void setContents(ItemStack[] items) {
        if (items.length != slots.length) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < items.length; ++i) {
            setItem(i, items[i]);
        }
    }

    @Override
    public boolean contains(int materialId) {
        return first(materialId) >= 0;
    }

    @Override
    public boolean contains(Material material) {
        return first(material) >= 0;
    }

    @Override
    public boolean contains(ItemStack item) {
        return first(item) >= 0;
    }

    @Override
    public boolean contains(Material material, int amount) {
        return contains(material.getId(), amount);
    }

    @Override
    public boolean contains(ItemStack item, int amount) {
        return contains(item.getTypeId(), amount);
    }

    @Override
    public boolean contains(int materialId, int amount) {
        HashMap<Integer, ? extends ItemStack> found = all(materialId);
        int total = 0;
        for (ItemStack stack : found.values()) {
            total += stack.getAmount();
        }
        return total >= amount;
    }

    @Override
    public HashMap<Integer, ItemStack> all(Material material) {
        return all(material.getId());
    }

    @Override
    public HashMap<Integer, ItemStack> all(int materialId) {
        HashMap<Integer, ItemStack> result = new HashMap<Integer, ItemStack>();
        for (int i = 0; i < slots.length; ++i) {
            if (slots[i].getTypeId() == materialId) {
                result.put(i, slots[i]);
            }
        }
        return result;
    }

    @Override
    public HashMap<Integer, ItemStack> all(ItemStack item) {
        HashMap<Integer, ItemStack> result = new HashMap<Integer, ItemStack>();
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] != null && slots[i].equals(item)) {
                result.put(i, slots[i]);
            }
        }
        return result;
    }

    @Override
    public int first(Material material) {
        return first(material.getId());
    }

    @Override
    public int first(int materialId) {
        for (int i = 0; i < slots.length; ++i) {
            if (slots[i] != null && slots[i].getTypeId() == materialId)
                return i;
        }
        return -1;
    }

    @Override
    public int first(ItemStack item) {
        for (int i = 0; i < slots.length; ++i) {
            if (slots[i] != null && slots[i].equals(item))
                return i;
        }
        return -1;
    }

    @Override
    public int firstEmpty() {
        for (int i = 0; i < slots.length; ++i) {
            if (slots[i] == null)
                return i;
        }
        return -1;
    }

    @Override
    public void remove(int materialId) {
        HashMap<Integer, ? extends ItemStack> stacks = all(materialId);
        for (Integer slot : stacks.keySet()) {
            setItem(slot, null);
        }
    }

    @Override
    public void remove(Material material) {
        HashMap<Integer, ? extends ItemStack> stacks = all(material);
        for (Integer slot : stacks.keySet()) {
            setItem(slot, null);
        }
    }

    @Override
    public void remove(ItemStack item) {
        HashMap<Integer, ? extends ItemStack> stacks = all(item);
        for (Integer slot : stacks.keySet()) {
            setItem(slot, null);
        }
    }

    @Override
    public void clear(int index) {
        setItem(index, null);
    }

    @Override
    public void clear() {
        for (int i = 0; i < slots.length; ++i) {
            clear(i);
        }
    }

}
