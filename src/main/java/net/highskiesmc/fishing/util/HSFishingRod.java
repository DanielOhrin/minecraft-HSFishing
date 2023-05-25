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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class that parses an HSFishingRod into readable values
 * Constructor requires a valid HSFishingRod ItemStack (Check before passing)
 * OR nothing and it will make a level 1 one
 */
public class HSFishingRod {
    private final NamespacedKey EXPERIENCE_KEY;
    private final NamespacedKey LEVEL_KEY;
    private final HSFishing MAIN;
    private int experience;
    private int level;

    public HSFishingRod(HSFishing main, ItemStack existingRod) {
        this.MAIN = main;
        this.EXPERIENCE_KEY = new NamespacedKey(this.MAIN, "rod-exp");
        this.LEVEL_KEY = new NamespacedKey(this.MAIN, "rod-level");

        // Parse existing Rod
        this.parseRod(existingRod);
    }

    public HSFishingRod(HSFishing main) {
        this.MAIN = main;
        this.EXPERIENCE_KEY = new NamespacedKey(this.MAIN, "rod-exp");
        this.LEVEL_KEY = new NamespacedKey(this.MAIN, "rod-level");

        // Construct new rod
        this.defaultRod();
    }

    private void parseRod(ItemStack existingRod) throws IllegalArgumentException {
        PersistentDataContainer pdc;
        if (existingRod.hasItemMeta()) {
            pdc = existingRod.getItemMeta().getPersistentDataContainer();
            if (pdc.has(this.LEVEL_KEY, PersistentDataType.INTEGER)) {
                this.experience = pdc.get(this.EXPERIENCE_KEY, PersistentDataType.INTEGER);
                this.level = pdc.get(this.LEVEL_KEY, PersistentDataType.INTEGER);
            } else {
                throw new IllegalArgumentException("Item received was not a valid HSFishingRod");
            }
        } else {
            throw new IllegalArgumentException("Item received was not a valid HSFishingRod");
        }

    }

    private void defaultRod() {
        this.experience = 0;
        this.level = 1;
    }

    /**
     * @return new Fishing Rod matching the current object's properties
     */
    public ItemStack getRod() {
        ItemStack rod = new ItemStack(Material.FISHING_ROD, 1);
        ItemMeta meta = rod.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        pdc.set(this.EXPERIENCE_KEY, PersistentDataType.INTEGER, this.experience);
        pdc.set(this.LEVEL_KEY, PersistentDataType.INTEGER, this.level);

        final ConfigurationSection CONFIG = this.MAIN.getConfig();
        final List<String> KEYS = new ArrayList<>(CONFIG.getKeys(false));
        ConfigurationSection ROD_CONFIG = null;
        // Figure out which config to use
        for (int i = KEYS.size() - 1; i >= 0; i--) {
            if (CONFIG.getInt(KEYS.get(i) + ".minimum-level") <= this.level) {
                ROD_CONFIG = CONFIG.getConfigurationSection(KEYS.get(i));
                break;
            }
        }

        if (ROD_CONFIG == null) {
            throw new NullPointerException("Could not find matching configuration section for the rod");
        }

        final String DISPLAY_NAME = this.getDisplayName(ROD_CONFIG);
        final List<String> LORE = this.getLore();


        meta.setDisplayName(DISPLAY_NAME);
        meta.setLore(LORE);
        rod.setItemMeta(meta);

        return rod;
    }

    public String getDisplayName(final ConfigurationSection ROD_CONFIG) {
        return ChatColor.translateAlternateColorCodes('&', ROD_CONFIG.getString("display-name")) + ' '
                + ChatColor.WHITE + '(' + ChatColor.GRAY + "Level " + this.level + ChatColor.WHITE + ')';
    }

    public List<String> getLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Experience: "
                + ChatColor.DARK_AQUA + this.experience + ChatColor.WHITE + "/" + ChatColor.RED + CustomLevelSystem.getExperienceRequiredForLevel(this.level));
        lore.add("");
        lore.add(ChatColor.GRAY + "Perks:");
        //TODO: List perks here

        return lore;
    }
}
