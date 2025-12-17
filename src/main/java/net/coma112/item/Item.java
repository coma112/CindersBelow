package net.coma112.item;

public abstract class Item {
    protected int currentDurability;
    protected int maxDurability;

    public Item() {
        this.maxDurability = getDurability();
        this.currentDurability = maxDurability;
    }

    public abstract String getName();
    public abstract ItemType getCategory();
    public abstract RarityType getRarity();
    public abstract int getDurability(); // Max durability
    public abstract int getAttackDamage();
    public abstract int getStaminaCost();
    public abstract int getDurabilityLoss();

    public int getCurrentDurability() {
        return currentDurability;
    }

    public int getMaxDurability() {
        return maxDurability;
    }

    public void setCurrentDurability(int durability) {
        this.currentDurability = Math.max(0, Math.min(durability, maxDurability));
    }

    public void damage(int amount) {
        currentDurability -= amount;
        if (currentDurability < 0) {
            currentDurability = 0;
        }
    }

    public void repair(int amount) {
        currentDurability += amount;
        if (currentDurability > maxDurability) {
            currentDurability = maxDurability;
        }
    }

    public boolean isBroken() {
        return currentDurability <= 0;
    }

    public float getDurabilityPercentage() {
        return (float) currentDurability / maxDurability;
    }

    public void use() {
        damage(getDurabilityLoss());
    }

    // TODO
    // az itemeknek van animációjuk, ezeket el kellene tárolni és lekezelni őket
    // emellett átadni a példányosításkor és lekezelni emberiesen.

    // TODO
    // letölteni valami ingyenes szar kardot animációval :d
}