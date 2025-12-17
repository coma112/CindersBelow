package net.coma112.inventory;

import lombok.Getter;

@Getter
public enum ArmorSlot {
    HELMET(0),
    CHESTPLATE(1),
    LEGGINGS(2),
    BOOTS(3),
    RING(4);

    private final int index;

    ArmorSlot(int index) {
        this.index = index;
    }
}
