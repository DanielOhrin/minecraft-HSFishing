package net.highskiesmc.fishing.util;

import net.highskiesmc.fishing.HSFishing;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that parses an HSFishingRod into readable values
 * Constructor requires a valid HSFishingRod ItemStack (Check before passing)
 * OR nothing and it will make a level 1 one
 */
public class HSFishingRod {
    private ConfigurationSection ROD_CONFIG;
    private final NamespacedKey LEVEL_KEY;
    private final NamespacedKey TOTAL_EXPERIENCE_KEY;
    private final NamespacedKey CURRENT_EXPERIENCE_KEY; // Current experience earned towards next level
    private final NamespacedKey ITEM_LUCK_KEY;
    private final NamespacedKey EXPERIENCE_MULTIPLIER_KEY;
    private final NamespacedKey TOTAL_ITEMS_CAUGHT_KEY;
    private final HSFishing MAIN;
    private double totalExperience = 0;
    private double currentExperience = 0;
    private double itemLuck = 0;
    private int totalItemsCaught = 0;
    private int level = 1;
    private double experienceMultiplier = 1.0D;
    private final DropTable DROP_TABLE;

    public HSFishingRod(HSFishing main, ItemStack existingRod) throws IOException {
        this.MAIN = main;
        this.TOTAL_EXPERIENCE_KEY = new NamespacedKey(this.MAIN, "rod-total-experience");
        this.CURRENT_EXPERIENCE_KEY = new NamespacedKey(this.MAIN, "rod-current-experience");
        this.ITEM_LUCK_KEY = new NamespacedKey(this.MAIN, "rod-item-luck");
        this.TOTAL_ITEMS_CAUGHT_KEY = new NamespacedKey(this.MAIN, "rod-total-items-caught");
        this.LEVEL_KEY = new NamespacedKey(this.MAIN, "rod-level");

        // Parse existing Rod
        this.parseRod(existingRod);
        this.findRodConfig();

        // Fetch the drop table
        this.DROP_TABLE = new DropTable(this.ROD_CONFIG.getConfigurationSection("drop-table"));
    }

    public HSFishingRod(HSFishing main) throws IOException {
        this.MAIN = main;
        this.LEVEL_KEY = new NamespacedKey(this.MAIN, "rod-level");
        this.TOTAL_EXPERIENCE_KEY = new NamespacedKey(this.MAIN, "rod-total-experience");
        this.CURRENT_EXPERIENCE_KEY = new NamespacedKey(this.MAIN, "rod-current-experience");
        this.ITEM_LUCK_KEY = new NamespacedKey(this.MAIN, "rod-item-luck");
        this.EXPERIENCE_MULTIPLIER_KEY = new NamespacedKey(this.MAIN, "rod-experience-multiplier");
        this.TOTAL_ITEMS_CAUGHT_KEY = new NamespacedKey(this.MAIN, "rod-total-items-caught");

        // Construct new rod
        this.findRodConfig();

        // Fetch the drop table
        this.DROP_TABLE = new DropTable(this.ROD_CONFIG.getConfigurationSection("drop-table"));
    }

    private void parseRod(ItemStack existingRod) throws IllegalArgumentException {
        PersistentDataContainer pdc;
        if (existingRod.hasItemMeta()) {
            pdc = existingRod.getItemMeta().getPersistentDataContainer();
            if (pdc.has(this.LEVEL_KEY, PersistentDataType.INTEGER)) {
                this.level = pdc.get(this.LEVEL_KEY, PersistentDataType.INTEGER);
                this.totalExperience = pdc.get(this.TOTAL_EXPERIENCE_KEY, PersistentDataType.DOUBLE);
                this.currentExperience = pdc.get(this.CURRENT_EXPERIENCE_KEY, PersistentDataType.DOUBLE);
                this.itemLuck = pdc.get(this.ITEM_LUCK_KEY, PersistentDataType.DOUBLE);
                this.experienceMultiplier = pdc.get(this.EXPERIENCE_MULTIPLIER_KEY, PersistentDataType.DOUBLE);
                this.totalItemsCaught = pdc.get(this.TOTAL_ITEMS_CAUGHT_KEY, PersistentDataType.INTEGER);
            } else {
                throw new IllegalArgumentException("Item received was not a valid HSFishingRod");
            }
        } else {
            throw new IllegalArgumentException("Item received was not a valid HSFishingRod");
        }

    }

    /**
     * @return new Fishing Rod matching the current object's properties
     */
    public ItemStack getRod() {
        ItemStack rod = new ItemStack(Material.FISHING_ROD, 1);
        ItemMeta meta = rod.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        pdc.set(this.LEVEL_KEY, PersistentDataType.INTEGER, this.level);
        pdc.set(this.TOTAL_EXPERIENCE_KEY, PersistentDataType.DOUBLE, this.totalExperience);
        pdc.set(this.CURRENT_EXPERIENCE_KEY, PersistentDataType.DOUBLE, this.currentExperience);
        pdc.set(this.ITEM_LUCK_KEY, PersistentDataType.DOUBLE, this.itemLuck);
        pdc.set(this.EXPERIENCE_MULTIPLIER_KEY, PersistentDataType.DOUBLE, this.experienceMultiplier);
        pdc.set(this.TOTAL_ITEMS_CAUGHT_KEY, PersistentDataType.INTEGER, this.totalItemsCaught);

        if (this.ROD_CONFIG == null) {
            throw new NullPointerException("Could not find matching configuration section for the rod");
        }

        final String DISPLAY_NAME = this.getDisplayName();
        final List<String> LORE = this.getLore();


        meta.setDisplayName(DISPLAY_NAME);
        meta.setLore(LORE);
        rod.setItemMeta(meta);

        return rod;
    }

    public String getDisplayName() {
        return ChatColor.translateAlternateColorCodes('&', this.ROD_CONFIG.getString("display-name")) + ' '
                + ChatColor.WHITE + '(' + ChatColor.GRAY + "Level " + this.level + ChatColor.WHITE + ')';
    }

    public List<String> getLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");

        // Add lore from ROD_CONFIG
        List<String> configLore = this.ROD_CONFIG.getStringList("lore");
        for (int i = 0; i < configLore.size(); i++) {
            configLore.set(i, ChatColor.translateAlternateColorCodes('&', configLore.get(i)));
        }
        lore.addAll(configLore);

        // Add rest of the lore
        lore.add("");
        lore.add(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Experience: "
                + ChatColor.DARK_AQUA + this.currentExperience + ChatColor.WHITE + "/" + ChatColor.RED + CustomLevelSystem.getExperienceRequiredForLevel(this.level));

        // List perks
        lore.add("");
        lore.add(ChatColor.WHITE.toString() + ChatColor.BOLD + "Perks");

        lore.add(ChatColor.YELLOW + "Xp Gain: " + ChatColor.AQUA + this.experienceMultiplier + 'x');
        if (this.itemLuck > 0) {
            lore.add(ChatColor.YELLOW + "Item Find: " + ChatColor.LIGHT_PURPLE + "+ " + this.itemLuck);
        }

        return lore;
    }

    /**
     * Sets the ConfigurationSection for the fishing rod
     */
    private void findRodConfig() {
        final ConfigurationSection CONFIG = this.MAIN.getConfig();
        final List<String> KEYS = new ArrayList<>(CONFIG.getKeys(false));
        // Figure out which config to use
        for (int i = KEYS.size() - 1; i >= 0; i--) {
            if (CONFIG.getInt(KEYS.get(i) + ".minimum-level") <= this.level) {
                this.ROD_CONFIG = CONFIG.getConfigurationSection(KEYS.get(i));
                break;
            }
        }
    }

    public ConfigurationSection getRodConfig() {
        return this.ROD_CONFIG;
    }

    public DropTable getDropTable() {
        return this.DROP_TABLE;
    }
}
