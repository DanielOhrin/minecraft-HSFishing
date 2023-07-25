package net.highskiesmc.fishing.inventories;

import com.mattisadev.mcore.inventory.GUI;
import net.highskiesmc.fishing.HSFishing;
import net.highskiesmc.fishing.util.HSFishingRod;
import net.highskiesmc.fishing.util.enums.Perk;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.util.*;

public class RodUpgradesGUI implements GUI {
    private final HSFishingRod ROD;

    public RodUpgradesGUI(HSFishingRod rod) {
        this.ROD = rod;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent paramInventoryClickEvent) {
        Player player = this.ROD.getPlayer();
        int skillPoints = this.ROD.getSkillPoints();
        int currentLevel;
        int levelupCost;

        switch (paramInventoryClickEvent.getRawSlot()) {
            case 10 -> {
                currentLevel = Perk.XP_GAIN.getLevel(this.ROD.getExperienceMultiplier());

                if (currentLevel == Perk.MAX_PERK_LEVEL) return;

                levelupCost = Perk.getNextLevelCost(currentLevel);

                if (skillPoints >= levelupCost) {
                    // Decrement the skill points + Upgrade Rod
                    this.ROD.setSkillPoints(skillPoints - levelupCost);
                    this.ROD.setExperienceMultiplier(Perk.XP_GAIN.getAmount(currentLevel + 1));

                    // Replace item in main hand with rod
                    player.getInventory().setItemInMainHand(this.ROD.getRod());

                    // Reopen Inventory
                    player.closeInventory();
                    Bukkit.getScheduler().runTaskLater(HSFishing.getPlugin(HSFishing.class), () -> {
                        player.openInventory(getInventory());
                    }, 1);

                    // Play sound
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                } else {
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                }
            }
            case 11 -> {
                currentLevel = Perk.FISHING_SPEED.getLevel(this.ROD.getFishingSpeed());

                if (currentLevel == Perk.MAX_PERK_LEVEL) return;

                levelupCost = Perk.getNextLevelCost(currentLevel);

                if (skillPoints >= levelupCost) {
                    // Decrement the skill points + Upgrade Rod
                    this.ROD.setSkillPoints(skillPoints - levelupCost);
                    this.ROD.setFishingSpeed(Perk.FISHING_SPEED.getAmount(currentLevel + 1));

                    // Replace item in main hand with rod
                    player.getInventory().setItemInMainHand(this.ROD.getRod());

                    // Reopen Inventory
                    player.closeInventory();
                    Bukkit.getScheduler().runTaskLater(HSFishing.getPlugin(HSFishing.class), () -> {
                        player.openInventory(getInventory());
                    }, 1);

                    // Play sound
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                } else {
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                }
            }
            case 13 -> {
                currentLevel = Perk.ITEM_FIND.getLevel(this.ROD.getItemLuck());

                if (currentLevel == Perk.MAX_PERK_LEVEL) return;

                levelupCost = Perk.getNextLevelCost(currentLevel);

                if (skillPoints >= levelupCost) {
                    // Decrement the skill points + Upgrade Rod
                    this.ROD.setSkillPoints(skillPoints - levelupCost);
                    this.ROD.setItemLuck(Perk.ITEM_FIND.getAmount(currentLevel + 1));

                    // Replace item in main hand with rod
                    player.getInventory().setItemInMainHand(this.ROD.getRod());

                    // Reopen Inventory
                    player.closeInventory();
                    Bukkit.getScheduler().runTaskLater(HSFishing.getPlugin(HSFishing.class), () -> {
                        player.openInventory(getInventory());
                    }, 1);

                    // Play sound
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                } else {
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                }
            }
            case 15 -> {
                currentLevel = Perk.DOUBLE_DROPS.getLevel(this.ROD.getDoubleDrops());

                if (currentLevel == Perk.MAX_PERK_LEVEL) return;

                levelupCost = Perk.getNextLevelCost(currentLevel);

                if (skillPoints >= levelupCost) {
                    // Decrement the skill points + Upgrade Rod
                    this.ROD.setSkillPoints(skillPoints - levelupCost);
                    this.ROD.setDoubleDrops(Perk.DOUBLE_DROPS.getAmount(currentLevel + 1));

                    // Replace item in main hand with rod
                    player.getInventory().setItemInMainHand(this.ROD.getRod());

                    // Reopen Inventory
                    player.closeInventory();
                    Bukkit.getScheduler().runTaskLater(HSFishing.getPlugin(HSFishing.class), () -> {
                        player.openInventory(getInventory());
                    }, 1);

                    // Play sound
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                } else {
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                }
            }
            case 16 -> {
                currentLevel = Perk.DOUBLE_XP.getLevel(this.ROD.getDoubleXp());

                if (currentLevel == Perk.MAX_PERK_LEVEL) return;

                levelupCost = Perk.getNextLevelCost(currentLevel);

                if (skillPoints >= levelupCost) {
                    // Decrement the skill points + Upgrade Rod
                    this.ROD.setSkillPoints(skillPoints - levelupCost);
                    this.ROD.setDoubleXp(Perk.DOUBLE_XP.getAmount(currentLevel + 1));

                    // Replace item in main hand with rod
                    player.getInventory().setItemInMainHand(this.ROD.getRod());

                    // Reopen Inventory
                    player.closeInventory();
                    Bukkit.getScheduler().runTaskLater(HSFishing.getPlugin(HSFishing.class), () -> {
                        player.openInventory(getInventory());
                    }, 1);

                    // Play sound
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                } else {
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                }
            }
        }
    }

    @Override
    public void onInventoryOpen(InventoryOpenEvent paramInventoryOpenEvent) {
        final HSFishing MAIN = HSFishing.getPlugin(HSFishing.class);

        Bukkit.getScheduler().runTaskLater(MAIN, () -> {
            try {
                new HSFishingRod(MAIN,
                        paramInventoryOpenEvent.getPlayer().getInventory().getItemInMainHand(),
                        (Player) paramInventoryOpenEvent.getPlayer());
            } catch (IllegalArgumentException | IOException ignore) {
                paramInventoryOpenEvent.getPlayer().closeInventory();
            }
        }, 5);
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent paramInventoryCloseEvent) {

    }

    @Override
    public void addContent(Inventory paramInventory) {
        ItemStack filler = getFillerItem();
        Map<Integer, ItemStack> items = new HashMap<>() {{
            put(10, getXpGainItem());
            put(11, getFishingSpeedItem());
            put(12, null);
            put(13, getItemFindItem());
            put(14, null);
            put(15, getDoubleDropsItem());
            put(16, getDoubleXpItem());
            put(22, getSkillPointsItem());
        }};

        for (int i = 0; i < paramInventory.getSize(); i++) {
            if (items.containsKey(i)) {
                ItemStack item = items.get(i);

                if (item != null) {
                    paramInventory.setItem(i, item);
                }

                continue;
            }

            paramInventory.setItem(i, filler);
        }
    }

    private @NonNull ItemStack getFillerItem() {
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(" ");
        item.setItemMeta(meta);

        return item;
    }

    private @NonNull ItemStack getSkillPointsItem() {
        ItemStack item = new ItemStack(Material.SUNFLOWER);

        ItemMeta meta = item.getItemMeta();
        int skillPoints = this.ROD.getSkillPoints();
        meta.setDisplayName(ChatColor.YELLOW + "Skill Points: " + ChatColor.LIGHT_PURPLE + skillPoints);
        item.setItemMeta(meta);

        return item;
    }

    private @NonNull ItemStack getXpGainItem() {
        ItemStack item = new ItemStack(Material.EXPERIENCE_BOTTLE);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Xp Gain");
        List<String> lore = new ArrayList<>(Arrays.asList(
                "",
                ChatColor.YELLOW + "Current: " + ChatColor.AQUA + this.ROD.getExperienceMultiplier() + "x"
        ));

        int currentLevel = Perk.XP_GAIN.getLevel(this.ROD.getExperienceMultiplier());
        boolean isMaxLevel = currentLevel == Perk.MAX_PERK_LEVEL;

        if (!isMaxLevel) {
            lore.add(ChatColor.YELLOW + "Next: " + ChatColor.AQUA + Perk.XP_GAIN.getAmount(currentLevel + 1) + "x");
            lore.add(ChatColor.YELLOW + "Max: " + ChatColor.AQUA + Perk.XP_GAIN.getAmount(Perk.MAX_PERK_LEVEL) + "x");
        }

        lore.addAll(Arrays.asList(
                "",
                ChatColor.AQUA + "Increases the amount of",
                ChatColor.LIGHT_PURPLE.toString() + ChatColor.UNDERLINE + "xp" + ChatColor.AQUA + " per catch",
                ""
        ));

        if (!isMaxLevel) {
            lore.add(ChatColor.WHITE.toString() + ChatColor.BOLD + "Cost: " + ChatColor.LIGHT_PURPLE + Perk.getNextLevelCost(currentLevel) + " " + ChatColor.YELLOW + "Skill Point");
        } else {
            lore.add(ChatColor.WHITE.toString() + ChatColor.BOLD + "MAX LEVEL");
        }

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    private @NonNull ItemStack getFishingSpeedItem() {
        ItemStack item = new ItemStack(Material.SUGAR);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Fishing Speed");
        List<String> lore = new ArrayList<>(Arrays.asList(
                "",
                ChatColor.YELLOW + "Current: " + ChatColor.AQUA + "+" + Double.valueOf(this.ROD.getFishingSpeed()).intValue() + "%"
        ));

        int currentLevel = Perk.FISHING_SPEED.getLevel(this.ROD.getFishingSpeed());
        boolean isMaxLevel = currentLevel == Perk.MAX_PERK_LEVEL;

        if (!isMaxLevel) {
            lore.add(ChatColor.YELLOW + "Next: " + ChatColor.AQUA + "+" + Double.valueOf(Perk.FISHING_SPEED.getAmount(currentLevel + 1)).intValue() + "%");
            lore.add(ChatColor.YELLOW + "Max: " + ChatColor.AQUA + "+" + Double.valueOf(Perk.FISHING_SPEED.getAmount(Perk.MAX_PERK_LEVEL)).intValue() + "%");
        }

        lore.addAll(Arrays.asList(
                "",
                ChatColor.AQUA + "Increases the " + ChatColor.LIGHT_PURPLE + ChatColor.UNDERLINE + "speed",
                ChatColor.AQUA + "of your fishing rod",
                ""
        ));

        if (!isMaxLevel) {
            lore.add(ChatColor.WHITE.toString() + ChatColor.BOLD + "Cost: " + ChatColor.LIGHT_PURPLE + Perk.getNextLevelCost(currentLevel) + " " + ChatColor.YELLOW + "Skill Point");
        } else {
            lore.add(ChatColor.WHITE.toString() + ChatColor.BOLD + "MAX LEVEL");
        }

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    private @NonNull ItemStack getItemFindItem() {
        ItemStack item = new ItemStack(Material.EMERALD);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Item Find");
        List<String> lore = new ArrayList<>(Arrays.asList(
                "",
                ChatColor.YELLOW + "Current: " + ChatColor.LIGHT_PURPLE + Double.valueOf(this.ROD.getItemLuck()).intValue()
        ));

        int currentLevel = Perk.ITEM_FIND.getLevel(this.ROD.getItemLuck());
        boolean isMaxLevel = currentLevel == Perk.MAX_PERK_LEVEL;

        if (!isMaxLevel) {
            lore.add(ChatColor.YELLOW + "Next: " + ChatColor.LIGHT_PURPLE + Double.valueOf(Perk.ITEM_FIND.getAmount(currentLevel + 1)).intValue());
            lore.add(ChatColor.YELLOW + "Max: " + ChatColor.LIGHT_PURPLE + Double.valueOf(Perk.ITEM_FIND.getAmount(Perk.MAX_PERK_LEVEL)).intValue());
        }

        lore.addAll(Arrays.asList(
                "",
                ChatColor.AQUA + "Helps your rod",
                ChatColor.AQUA + "catch " + ChatColor.LIGHT_PURPLE + ChatColor.UNDERLINE + "rarer" + ChatColor.AQUA + " drops",
                ""
        ));

        if (!isMaxLevel) {
            lore.add(ChatColor.WHITE.toString() + ChatColor.BOLD + "Cost: " + ChatColor.LIGHT_PURPLE + Perk.getNextLevelCost(currentLevel) + " " + ChatColor.YELLOW + "Skill Point");
        } else {
            lore.add(ChatColor.WHITE.toString() + ChatColor.BOLD + "MAX LEVEL");
        }

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    private @NonNull ItemStack getDoubleDropsItem() {
        ItemStack item = new ItemStack(Material.HOPPER);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Double Drops");
        List<String> lore = new ArrayList<>(Arrays.asList(
                "",
                ChatColor.YELLOW + "Current: " + ChatColor.WHITE + Double.valueOf(this.ROD.getDoubleDrops()).intValue() + "%"
        ));

        int currentLevel = Perk.DOUBLE_DROPS.getLevel(this.ROD.getDoubleDrops());
        boolean isMaxLevel = currentLevel == Perk.MAX_PERK_LEVEL;

        if (!isMaxLevel) {
            lore.add(ChatColor.YELLOW + "Next: " + ChatColor.WHITE + Double.valueOf(Perk.DOUBLE_DROPS.getAmount(currentLevel + 1)).intValue() + "%");
            lore.add(ChatColor.YELLOW + "Max: " + ChatColor.WHITE + Double.valueOf(Perk.DOUBLE_DROPS.getAmount(Perk.MAX_PERK_LEVEL)).intValue() + "%");
        }

        lore.addAll(Arrays.asList(
                "",
                ChatColor.AQUA + "Gives a chance for",
                ChatColor.LIGHT_PURPLE.toString() + ChatColor.UNDERLINE + "double items",
                ""
        ));

        if (!isMaxLevel) {
            lore.add(ChatColor.WHITE.toString() + ChatColor.BOLD + "Cost: " + ChatColor.LIGHT_PURPLE + Perk.getNextLevelCost(currentLevel) + " " + ChatColor.YELLOW + "Skill Point");
        } else {
            lore.add(ChatColor.WHITE.toString() + ChatColor.BOLD + "MAX LEVEL");
        }

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    private @NonNull ItemStack getDoubleXpItem() {
        ItemStack item = new ItemStack(Material.ANVIL);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Double Xp");
        List<String> lore = new ArrayList<>(Arrays.asList(
                "",
                ChatColor.YELLOW + "Current: " + ChatColor.WHITE + Double.valueOf(this.ROD.getDoubleXp()).intValue() + "%"
        ));

        int currentLevel = Perk.DOUBLE_XP.getLevel(this.ROD.getDoubleXp());
        boolean isMaxLevel = currentLevel == Perk.MAX_PERK_LEVEL;

        if (!isMaxLevel) {
            lore.add(ChatColor.YELLOW + "Next: " + ChatColor.WHITE + Double.valueOf(Perk.DOUBLE_XP.getAmount(currentLevel + 1)).intValue() + "%");
            lore.add(ChatColor.YELLOW + "Max: " + ChatColor.WHITE + Double.valueOf(Perk.DOUBLE_XP.getAmount(Perk.MAX_PERK_LEVEL)).intValue() + "%");
        }

        lore.addAll(Arrays.asList(
                "",
                ChatColor.AQUA + "Gives a chance for",
                ChatColor.LIGHT_PURPLE.toString() + ChatColor.UNDERLINE + "double xp",
                ""
        ));

        if (!isMaxLevel) {
            lore.add(ChatColor.WHITE.toString() + ChatColor.BOLD + "Cost: " + ChatColor.LIGHT_PURPLE + Perk.getNextLevelCost(currentLevel) + " " + ChatColor.YELLOW + "Skill Point");
        } else {
            lore.add(ChatColor.WHITE.toString() + ChatColor.BOLD + "MAX LEVEL");
        }

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    @Override
    @NonNull
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, 27, ChatColor.AQUA.toString() + ChatColor.BOLD + "Rod Upgrades");

        addContent(inv);

        return inv;
    }
}
