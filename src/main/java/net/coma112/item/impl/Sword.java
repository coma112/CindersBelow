package net.coma112.item.impl;

import net.coma112.item.Item;
import net.coma112.item.ItemType;
import net.coma112.item.RarityType;

public class Sword extends Item {

    @Override
    public String getName() {
        return "Kard";
    }

    @Override
    public ItemType getCategory() {
        return ItemType.SWORD;
    }

    @Override
    public RarityType getRarity() {
        return RarityType.LEGENDARY;
    }

    @Override
    public int getDurability() {
        return 100;
    }

    @Override
    public int getAttackDamage() {
        return 25;
    }

    @Override
    public int getStaminaCost() {
        return 10;
    }

    @Override
    public int getDurabilityLoss() {
        return 5;
    }
}