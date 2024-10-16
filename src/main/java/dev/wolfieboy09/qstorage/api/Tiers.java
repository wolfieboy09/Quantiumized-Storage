package dev.wolfieboy09.qstorage.api;


import net.minecraft.world.item.Rarity;

public enum Tiers  {
    BASIC(Rarity.valueOf(Tiers.class, "basic")),
    ADVANCED(Rarity.valueOf(Tiers.class, "advanced"));

    public Rarity rarity;

    <T extends Enum<T>> Tiers(T basic) {}
}
