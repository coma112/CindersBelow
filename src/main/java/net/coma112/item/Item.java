package net.coma112.item;

public abstract class Item {
    public abstract String getName();
    public abstract ItemType getCategory();
    public abstract RarityType getRarity();
    public abstract int getDurability();
    // TODO
    // az itemeknek van animációjuk, ezeket el kellene tárolni és lekezelni őket
    // emellett átadni a példányosításkor és lekezelni emberiesen.

    // TODO
    // letölteni valami ingyenes szar kardot animációval :d
}
