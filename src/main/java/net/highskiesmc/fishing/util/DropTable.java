package net.highskiesmc.fishing.util;

import net.highskiesmc.fishing.util.enums.Rarity;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class DropTable {
    private final List<DropEntry> DROP_ENTRIES;
    private final Random RANDOM;
    private double extraWeight;

    public DropTable(final ConfigurationSection DROPS_LIST) throws IOException {
        this.DROP_ENTRIES = new ArrayList<>();
        this.RANDOM = new Random();

        for (String key : DROPS_LIST.getKeys(false)) {
            ConfigurationSection DROP_CONFIG = DROPS_LIST.getConfigurationSection(key);

            ItemStack item = ItemSerializer.deserialize(DROP_CONFIG.getString("item"));
            double weight = DROP_CONFIG.getDouble("weight");
            int experience = DROP_CONFIG.getInt("experience");

            this.addDropEntry(new DropEntry(item, weight, experience));
        }

        // Sort the drops in descending order by weight
        this.DROP_ENTRIES.sort(Comparator.comparingDouble(DropEntry::getWeight).reversed());
    }

    public void addDropEntry(DropEntry dropEntry) {
        this.DROP_ENTRIES.add(dropEntry);
    }

    public void addWeight(double weight) {
        this.extraWeight += weight;
    }

    public DropEntry getRandomDrop() {
        double totalWeight = this.DROP_ENTRIES.stream().mapToDouble(DropEntry::getWeight).sum();
        double randomWeight = this.RANDOM.nextDouble(totalWeight) + 1; // Add 1 because bound is exclusive

        if (randomWeight + this.extraWeight <= totalWeight) {
            randomWeight += this.extraWeight;
        }

        for (DropEntry dropEntry : this.DROP_ENTRIES) {
            randomWeight -= dropEntry.getWeight();
            if (randomWeight <= 0) {
                dropEntry.setRarity(this.getRarity(dropEntry.getWeight() / totalWeight));
                return dropEntry;
            }
        }

        return null; // No item selected
    }

    private Rarity getRarity(double dropChance) {
        if (dropChance > 0.40D) {
            return Rarity.COMMON;
        } else if (dropChance > 0.15D) {
            return Rarity.UNCOMMON;
        } else if (dropChance > 0.05D) {
            return Rarity.RARE;
        } else if (dropChance > 0.01D) {
            return Rarity.EPIC;
        } else {
            return Rarity.LEGENDARY;
        }
    }
}

