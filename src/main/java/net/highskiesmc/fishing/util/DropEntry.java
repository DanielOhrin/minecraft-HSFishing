package net.highskiesmc.fishing.util;

import org.bukkit.inventory.ItemStack;

public class DropEntry {
    private final ItemStack ITEMSTACK;
    private final double WEIGHT;
    private final double EXPERIENCE;

    public DropEntry(ItemStack item, double weight, double experience) {
        this.ITEMSTACK= item;
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
}

