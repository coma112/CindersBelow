package net.coma112.inventory.impl;

import lombok.Getter;
import net.coma112.entity.impl.Player;
import net.coma112.inventory.ArmorSlot;
import net.coma112.inventory.Inventory;
import net.coma112.item.Item;
import org.jetbrains.annotations.NotNull;

public class PlayerInventory implements Inventory {
    @Getter private final Item[] items;
    private final int size;
    @Getter private boolean isOpen = false;

    private static final int HOTBAR_SIZE = 9;
    @Getter private int selectedHotbarSlot = 0;

    @Getter private final Item[] armorSlots = new Item[5];

    public PlayerInventory(int size) {
        this.size = size;
        this.items = new Item[size];
    }

    @Override
    public void open(Player player) {
        isOpen = true;
    }

    @Override
    public void close(Player player) {
        isOpen = false;
    }

    @Override
    public Item getItem(int slot) {
        if (slot >= 0 && slot < size) {
            return items[slot];
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public void setItem(int slot, Item item) {
        if (slot >= 0 && slot < size) {
            items[slot] = item;
        }
    }

    public boolean addItem(Item item) {
        for (int i = 0; i < size; i++) {
            if (items[i] == null) {
                items[i] = item;
                return true;
            }
        }
        return false;
    }

    public void removeItem(int slot) {
        if (slot >= 0 && slot < size) {
            items[slot] = null;
        }
    }

    public int getItemCount() {
        int count = 0;
        for (Item item : items) {
            if (item != null) count++;
        }
        return count;
    }

    public Item getHotbarItem(int slot) {
        if (slot >= 0 && slot < HOTBAR_SIZE) {
            return items[slot];
        }
        return null;
    }

    public Item getSelectedHotbarItem() {
        return getHotbarItem(selectedHotbarSlot);
    }

    public void setSelectedHotbarSlot(int slot) {
        if (slot >= 0 && slot < HOTBAR_SIZE) {
            this.selectedHotbarSlot = slot;
        }
    }

    public int getHotbarSize() {
        return HOTBAR_SIZE;
    }

    public Item getArmorItem(@NotNull ArmorSlot slot) {
        return armorSlots[slot.getIndex()];
    }

    public void setArmorItem(@NotNull ArmorSlot slot, Item item) {
        armorSlots[slot.getIndex()] = item;
    }

    public void removeArmorItem(@NotNull ArmorSlot slot) {
        armorSlots[slot.getIndex()] = null;
    }
}