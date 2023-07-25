package net.highskiesmc.fishing.util;

import net.highskiesmc.fishing.HSFishing;
import net.highskiesmc.fishing.events.events.RodLevelUpEvent;
import net.highskiesmc.fishing.events.events.RodMilestoneUnlockedEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.common.value.qual.IntRange;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that parses an HSFishingRod into readable values
 * Constructor requires a valid HSFishingRod ItemStack (Check before passing)
 * OR nothing and it will make a level 1 one
 */
public class HSFishingRod {
    private ConfigurationSection ROD_CONFIG;
    private final NamespacedKey TOTAL_EXPERIENCE_KEY;
    private final NamespacedKey CURRENT_EXPERIENCE_KEY; // Current experience earned towards next level
    private final NamespacedKey LEVEL_KEY;
    private final NamespacedKey EXPERIENCE_MULTIPLIER_KEY;
    private final NamespacedKey FISHING_SPEED_KEY;
    private final NamespacedKey ITEM_LUCK_KEY;
    private final NamespacedKey DOUBLE_DROPS_KEY;
    private final NamespacedKey DOUBLE_XP_KEY;
    private final NamespacedKey TOTAL_ITEMS_CAUGHT_KEY;
    private final NamespacedKey SKILL_POINTS_KEY;
    private final HSFishing MAIN;
    private final Player PLAYER;
    private ItemMeta oldMeta = null;
    private double totalExperience = 0;
    private double currentExperience = 0;
    private int totalItemsCaught = 0;
    private final DropTable DROP_TABLE;
    private int level = 1;
    private int currentMilestone;
    private double experienceMultiplier = 1;
    private double itemLuck = 0;
    private double fishingSpeed = 0;
    private double doubleDrops = 0;
    private double doubleXp = 0;
    private int skillPoints = 0;

    public HSFishingRod(HSFishing main, ItemStack existingRod, Player player) throws IOException,
            IllegalArgumentException {
        this.MAIN = main;
        this.LEVEL_KEY = new NamespacedKey(this.MAIN, "rod-level");
        this.TOTAL_EXPERIENCE_KEY = new NamespacedKey(this.MAIN, "rod-total-experience");
        this.CURRENT_EXPERIENCE_KEY = new NamespacedKey(this.MAIN, "rod-current-experience");
        this.EXPERIENCE_MULTIPLIER_KEY = new NamespacedKey(this.MAIN, "rod-experience-multiplier");
        this.FISHING_SPEED_KEY = new NamespacedKey(this.MAIN, "rod-fishing-speed");
        this.ITEM_LUCK_KEY = new NamespacedKey(this.MAIN, "rod-item-luck");
        this.DOUBLE_DROPS_KEY = new NamespacedKey(this.MAIN, "rod-double-drops");
        this.DOUBLE_XP_KEY = new NamespacedKey(this.MAIN, "rod-double-xp");
        this.TOTAL_ITEMS_CAUGHT_KEY = new NamespacedKey(this.MAIN, "rod-total-items-caught");
        this.SKILL_POINTS_KEY = new NamespacedKey(this.MAIN, "rod-skill-points");

        // Parse existing Rod
        this.parseRod(existingRod);
        this.findRodConfig(null);

        // Fetch the drop table
        this.DROP_TABLE = new DropTable(this.ROD_CONFIG.getConfigurationSection("drop-table"));

        this.PLAYER = player;
    }

    public HSFishingRod(HSFishing main, Player player) throws IOException {
        this.MAIN = main;
        this.LEVEL_KEY = new NamespacedKey(this.MAIN, "rod-level");
        this.TOTAL_EXPERIENCE_KEY = new NamespacedKey(this.MAIN, "rod-total-experience");
        this.CURRENT_EXPERIENCE_KEY = new NamespacedKey(this.MAIN, "rod-current-experience");
        this.EXPERIENCE_MULTIPLIER_KEY = new NamespacedKey(this.MAIN, "rod-experience-multiplier");
        this.FISHING_SPEED_KEY = new NamespacedKey(this.MAIN, "rod-fishing-speed");
        this.ITEM_LUCK_KEY = new NamespacedKey(this.MAIN, "rod-item-luck");
        this.DOUBLE_DROPS_KEY = new NamespacedKey(this.MAIN, "rod-double-drops");
        this.DOUBLE_XP_KEY = new NamespacedKey(this.MAIN, "rod-double-xp");
        this.TOTAL_ITEMS_CAUGHT_KEY = new NamespacedKey(this.MAIN, "rod-total-items-caught");
        this.SKILL_POINTS_KEY = new NamespacedKey(this.MAIN, "rod-skill-points");

        // Construct new rod
        this.findRodConfig(null);

        // Fetch the drop table
        this.DROP_TABLE = new DropTable(this.ROD_CONFIG.getConfigurationSection("drop-table"));

        this.PLAYER = player;
    }

    /**
     * Creates a new HSFishingRod from an existing one
     *
     * @param existingRod Fishing rod
     * @throws IllegalArgumentException Provided rod was invalid
     */
    private void parseRod(ItemStack existingRod) throws IllegalArgumentException {
        PersistentDataContainer pdc;
        if (existingRod.hasItemMeta()) {
            this.oldMeta = existingRod.getItemMeta();
            pdc = existingRod.getItemMeta().getPersistentDataContainer();
            if (pdc.has(this.LEVEL_KEY, PersistentDataType.INTEGER)) {
                this.level = pdc.get(this.LEVEL_KEY, PersistentDataType.INTEGER);
                this.totalExperience = pdc.get(this.TOTAL_EXPERIENCE_KEY, PersistentDataType.DOUBLE);
                this.currentExperience = pdc.get(this.CURRENT_EXPERIENCE_KEY, PersistentDataType.DOUBLE);
                this.experienceMultiplier = pdc.get(this.EXPERIENCE_MULTIPLIER_KEY, PersistentDataType.DOUBLE);
                this.fishingSpeed = pdc.get(this.FISHING_SPEED_KEY, PersistentDataType.DOUBLE);
                this.itemLuck = pdc.get(this.ITEM_LUCK_KEY, PersistentDataType.DOUBLE);
                this.doubleDrops = pdc.get(this.DOUBLE_DROPS_KEY, PersistentDataType.DOUBLE);
                this.doubleXp = pdc.get(this.DOUBLE_XP_KEY, PersistentDataType.DOUBLE);
                this.totalItemsCaught = pdc.get(this.TOTAL_ITEMS_CAUGHT_KEY, PersistentDataType.INTEGER);
                this.skillPoints = pdc.get(this.SKILL_POINTS_KEY, PersistentDataType.INTEGER);
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
        ItemMeta meta = oldMeta == null ? rod.getItemMeta() : oldMeta;
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        pdc.set(this.LEVEL_KEY, PersistentDataType.INTEGER, this.level);
        pdc.set(this.TOTAL_EXPERIENCE_KEY, PersistentDataType.DOUBLE, this.totalExperience);
        pdc.set(this.CURRENT_EXPERIENCE_KEY, PersistentDataType.DOUBLE, this.currentExperience);
        pdc.set(this.EXPERIENCE_MULTIPLIER_KEY, PersistentDataType.DOUBLE, this.experienceMultiplier);
        pdc.set(this.FISHING_SPEED_KEY, PersistentDataType.DOUBLE, this.fishingSpeed);
        pdc.set(this.ITEM_LUCK_KEY, PersistentDataType.DOUBLE, this.itemLuck);
        pdc.set(this.DOUBLE_DROPS_KEY, PersistentDataType.DOUBLE, this.doubleDrops);
        pdc.set(this.DOUBLE_XP_KEY, PersistentDataType.DOUBLE, this.doubleXp);
        pdc.set(this.TOTAL_ITEMS_CAUGHT_KEY, PersistentDataType.INTEGER, this.totalItemsCaught);
        pdc.set(this.SKILL_POINTS_KEY, PersistentDataType.INTEGER, this.skillPoints);

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

    /**
     * @return Display name of the current rod
     */
    public String getDisplayName() {
        return ChatColor.translateAlternateColorCodes('&', this.ROD_CONFIG.getString("display-name")) + ' '
                + ChatColor.WHITE + '(' + ChatColor.GRAY + "Level " + this.level + ChatColor.WHITE + ')';
    }

    /**
     * @return Lore of the current rod
     */
    public List<String> getLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");

        // Add lore from ROD_CONFIG
        List<String> configLore = this.ROD_CONFIG.getStringList("lore");
        configLore.replaceAll(textToTranslate -> ChatColor.translateAlternateColorCodes('&', textToTranslate));
        lore.addAll(configLore);

        // Add rest of the lore
        lore.add("");
        if (this.level < CustomLevelSystem.MAX_LEVEL) {
            lore.add(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Experience: "
                    + ChatColor.DARK_AQUA + (new DecimalFormat("#.##")).format(this.currentExperience) + ChatColor.WHITE + "/" +
                    ChatColor.RED + CustomLevelSystem.getExperienceRequiredForLevel(this.level + 1));
            if (this.currentExperience == CustomLevelSystem.getExperienceRequiredForLevel(this.level + 1)) {
                // Milestone unlocked lore
                lore.add(ChatColor.WHITE + "^ " + ChatColor.LIGHT_PURPLE + "/upgraderod");
            }
        } else {
            lore.add(ChatColor.YELLOW.toString() + ChatColor.BOLD + "MAX LEVEL");
        }

        // List perks
        lore.add("");
        lore.add(ChatColor.WHITE.toString() + ChatColor.BOLD + "Perks");

        lore.add(ChatColor.YELLOW + "Xp Gain: " + ChatColor.AQUA + (new DecimalFormat("#.##")).format(this.experienceMultiplier) + 'x');

        if (this.fishingSpeed > 0) {
            lore.add(ChatColor.YELLOW + "Fishing Speed: " + ChatColor.AQUA + "+" + Double.valueOf(this.fishingSpeed).intValue());
        }

        if (this.itemLuck > 0) {
            lore.add(ChatColor.YELLOW + "Item Find: " + ChatColor.LIGHT_PURPLE + "+" + new DecimalFormat("#.##").format(this.itemLuck));
        }

        if (this.doubleDrops > 0) {
            lore.add(ChatColor.YELLOW + "Double Drops: " + ChatColor.WHITE + new DecimalFormat("#.##").format(this.doubleDrops) + "%");
        }

        if (this.doubleXp > 0) {
            lore.add(ChatColor.YELLOW + "Double Xp: " + ChatColor.WHITE + new DecimalFormat("#.##").format(this.doubleXp) + "%");
        }

        lore.add("");
        lore.add(ChatColor.GOLD + "Total Drops: " + ChatColor.AQUA + this.totalItemsCaught);

        return lore;
    }

    /**
     * If null is provided, it will set the config for the rod
     *
     * @param level Optional level to search for
     * @return Milestone number
     */
    private Integer findRodConfig(Integer level) {
        final ConfigurationSection CONFIG = this.MAIN.getConfig();
        final List<String> KEYS = new ArrayList<>(CONFIG.getKeys(false));

        Integer rodMilestone = null;

        // Figure out which config to use
        for (int i = KEYS.size() - 1; i >= 0; i--) {
            if (CONFIG.getInt(KEYS.get(i) + ".minimum-level") <= (level == null ? this.level : level)) {
                rodMilestone = i + 1;
                if (level == null) {
                    this.currentMilestone = i + 1;
                    this.ROD_CONFIG = CONFIG.getConfigurationSection(KEYS.get(i));
                }
                break;
            }
        }

        return rodMilestone;
    }

    /**
     * Adds experience to the rod and handles upgrading level/checking for milestone upgrades.
     *
     * @param experience Amount of experience to add
     */
    public void addExperience(double experience) {
        this.totalExperience += experience;

        // Check if they were notified of the potential milestone unlocked
        boolean wasNotified = true;
        if (this.level != CustomLevelSystem.MAX_LEVEL) {
            wasNotified = this.currentExperience == CustomLevelSystem.getExperienceRequiredForLevel(this.level + 1);
        }

        try {
            this.currentExperience = Math.min(CustomLevelSystem.getExperienceRequiredForLevel(this.level + 1),
                    this.currentExperience + experience);
        } catch (IllegalArgumentException ignored) {
            this.currentExperience = 0;
        }
        // Try to level up the rod
        int nextLevel = CustomLevelSystem.MAX_LEVEL;
        if (this.level != CustomLevelSystem.MAX_LEVEL) {
            nextLevel = CustomLevelSystem.getNextLevel(this.level, this.currentExperience);
        }

        if (nextLevel > this.level) {
            // Check if new level was a milestone upgrade
            boolean isMilestoneUpgrade = wasNotified || findRodConfig(nextLevel) > this.currentMilestone;

            if (isMilestoneUpgrade) {
                if (!wasNotified) {
                    Bukkit.getPluginManager().callEvent(new RodMilestoneUnlockedEvent(this));
                }
            } else {
                // Update necessary data and call custom event
                this.upgrade();

                // Grant skill point(s)
                int skillPointsGiven = 1;
                this.skillPoints += skillPointsGiven;

                // Update the rod configuration
                findRodConfig(null);

                // Call the custom HSRodLevelUpEvent
                Bukkit.getPluginManager().callEvent(new RodLevelUpEvent(this, skillPointsGiven));
            }
        }
    }

    /**
     * Upgrades a rod to the next level
     */
    public void upgrade() {
        this.currentExperience = 0;
        this.level = Math.min(this.level + 1, CustomLevelSystem.MAX_LEVEL);
    }

    public ConfigurationSection getRodConfig() {
        return this.ROD_CONFIG;
    }

    public DropTable getDropTable() {
        return this.DROP_TABLE;
    }

    public void setItemLuck(double newLuck) {
        this.itemLuck = newLuck;
    }

    public void setExperienceMultiplier(double newMulti) {
        this.experienceMultiplier = newMulti;
    }

    public void setLevel(int level) {
        if (level <= CustomLevelSystem.MAX_LEVEL && level >= 1) {
            this.level = level;
            this.currentExperience = 0;
            findRodConfig(null);
        }
    }

    public double getItemLuck() {
        return this.itemLuck;
    }

    public double getExperienceMultiplier() {
        return this.experienceMultiplier;
    }

    /**
     * Adds to the rod's total items caught
     *
     * @param amount Amount of items to add
     */
    public void addCaughtItems(int amount) {
        this.totalItemsCaught += amount;
    }

    public int getLevel() {
        return this.level;
    }

    public Player getPlayer() {
        return this.PLAYER;
    }

    public int getCurrentMilestone() {
        return this.currentMilestone;
    }

    /**
     * Tries to upgrade the rod's milestone
     *
     * @throws IllegalStateException When the rod is not ready to upgrade
     */
    public void upgradeMilestone(boolean ignoreLevel) throws IllegalStateException {
        if (!ignoreLevel) {
            if (this.currentExperience == CustomLevelSystem.getExperienceRequiredForLevel(this.level + 1)) {
                this.upgrade();
                this.findRodConfig(null);
            } else {
                throw new IllegalStateException("Rod is not ready to upgrade its milestone.");
            }
        } else {
            this.currentExperience = 0;
            List<String> keys = new ArrayList<>(this.MAIN.getConfig().getKeys(false));
            int currentIndex = keys.indexOf(this.ROD_CONFIG.getCurrentPath());
            if (currentIndex != keys.size() - 1) {
                this.level = this.MAIN.getConfig().getInt(keys.get(currentIndex + 1) + ".minimum-level");
                findRodConfig(null);
            } else {
                this.level = -1;
            }
        }
    }

    public double getFishingSpeed() {
        return this.fishingSpeed;
    }

    public void setFishingSpeed(double speed) {
        this.fishingSpeed = speed;
    }

    public double getDoubleDrops() {
        return this.doubleDrops;
    }

    public void setDoubleDrops(double chance) {
        this.doubleDrops = chance;
    }

    public double getDoubleXp() {
        return this.doubleXp;
    }

    public void setDoubleXp(double chance) {
        this.doubleXp = chance;
    }

    public int getSkillPoints() {
        return this.skillPoints;
    }

    public void setSkillPoints(@IntRange(from = 0) int skillPoints) {
        this.skillPoints = skillPoints;
    }
}
