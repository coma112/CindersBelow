package net.coma112.inventory.impl;

import net.coma112.entity.impl.Player;
import net.coma112.inventory.Inventory;
import net.coma112.item.Item;

public class PlayerInventory implements Inventory {
    private final Item[] items;
    private final int size;
    private boolean isOpen = false;

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

    public boolean isOpen() {
        return isOpen;
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

    public Item[] getItems() {
        return items;
    }
}