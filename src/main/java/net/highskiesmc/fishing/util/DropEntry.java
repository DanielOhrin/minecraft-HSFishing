package net.highskiesmc.fishing.util;

import net.highskiesmc.fishing.util.enums.Rarity;
import org.bukkit.inventory.ItemStack;

public class DropEntry {
    private final ItemStack ITEMSTACK;
    private final double WEIGHT;
    private double experience;
    private Rarity rarity;

    public DropEntry(ItemStack item, double weight, double experience) {
        this.ITEMSTACK = item;
        this.WEIGHT = weight;
        this.experience = experience;
    }

    public ItemStack getItemStack() {
        return this.ITEMSTACK;
    }

    public int getAmount() {
        return this.ITEMSTACK.getAmount();
    }

    public void setAmount(int newAmount) {
        this.ITEMSTACK.setAmount(newAmount);
    }

    public double getWeight() {
        return this.WEIGHT;
    }

    public double getExperience() {
        return this.experience;
    }

    public void setExperience(double newExperience) {
        this.experience = newExperience;
    }

    public Rarity getRarity() {
        return this.rarity;
    }

    public void setRarity(Rarity pRarity) {
        this.rarity = pRarity;
    }
}

