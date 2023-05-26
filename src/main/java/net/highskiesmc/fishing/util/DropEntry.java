package net.highskiesmc.fishing.util;

import net.highskiesmc.fishing.util.enums.Rarity;
import org.bukkit.inventory.ItemStack;

public class DropEntry {
    private final ItemStack ITEMSTACK;
    private final double WEIGHT;
    private final double EXPERIENCE;
    private Rarity rarity;

    public DropEntry(ItemStack item, double weight, double experience) {
        this.ITEMSTACK = item;
        this.WEIGHT = weight;
        this.EXPERIENCE = experience;
    }

    public ItemStack getItemStack() {
        return this.ITEMSTACK;
    }

    public double getWeight() {
        return this.WEIGHT;
    }

    public double getExperience() {
        return this.EXPERIENCE;
    }

    public void setRarity(Rarity pRarity) {
        this.rarity = pRarity;
    }

    public Rarity getRarity() {
        return this.rarity;
    }
}

