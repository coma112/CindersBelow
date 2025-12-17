package net.coma112.inventory;

import net.coma112.entity.impl.Player;
import net.coma112.item.Item;

public interface Inventory {
    void open(Player player);

    void close(Player player);

    Item getItem(int slot);

    int getSize();
}
