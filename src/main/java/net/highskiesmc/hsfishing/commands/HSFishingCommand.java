package net.highskiesmc.hsfishing.commands;

import net.highskiesmc.hsfishing.HSFishing;
import net.highskiesmc.hsfishing.util.CustomLevelSystem;
import net.highskiesmc.hsfishing.util.HSFishingRod;
import net.highskiesmc.hsfishing.util.ItemSerializer;
import net.highskiesmc.hsfishing.util.LogUtils;
import net.highskiesmc.hsfishing.util.enums.Perk;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

public class HSFishingCommand implements CommandExecutor {
    private final static String USAGE_GIVE = "/hsfishing rod give <player> [level]";
    private final static String USAGE_MAIN = "/hsfishing <rod>";
    private final static String USAGE_ROD = "hsfishing rod <give/set/add-drop/add-perk>";
    private final static String USAGE_ROD_ADDDROP = "hsfishing rod add-drop <rod-key> <drop-key> <weight> <experience>";
    private final HSFishing MAIN;

    public HSFishingCommand(HSFishing main) {
        this.MAIN = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "rod" -> {
                    return this.rod(sender, args);
                }
                case "reload" -> {
                    return this.reload(sender);
                }
                default -> {
                }
            }
        }
        return LogUtils.error(sender, USAGE_MAIN, this.MAIN);
    }

    private boolean reload(CommandSender sender) {
        if (sender.hasPermission("hsfishing.reload")) {
            this.MAIN.reloadConfig();
            return LogUtils.success(sender, LogUtils.SUCCESS, this.MAIN);
        } else {
            return LogUtils.error(sender, LogUtils.ERROR_PERMISSION, this.MAIN);
        }
    }

    private boolean rod(CommandSender sender, String[] args) {
        if (args.length > 1) {
            switch (args[1].toLowerCase()) {
                case "give" -> {
                    return giveRod(sender, args);
                }
                case "add-drop" -> {
                    return addDrop(sender, args);
                }
                case "set" -> {
                    return set(sender, args);
                }
            }
        }

        return LogUtils.usage(sender, USAGE_ROD, this.MAIN);
    }

    private boolean giveRod(CommandSender sender, String[] args) {
        if (sender.hasPermission("hsfishing.rod.give")) {

            if (args.length >= 3) {
                Player player = Bukkit.getPlayerExact(args[2]);

                if (player != null) {
                    int level;

                    try {
                        level = Integer.parseInt(args[3]);
                    } catch (IllegalArgumentException | IndexOutOfBoundsException ignored) {
                        level = 1;
                    }

                    try {
                        HSFishingRod rod = new HSFishingRod(this.MAIN, player);
                        rod.setLevel(level);
                        player.getInventory().addItem(rod.getRod());
                        return LogUtils.success(sender, LogUtils.SUCCESS, this.MAIN);
                    } catch (IOException ex) {
                        this.MAIN.getLogger().severe(Arrays.toString(ex.getStackTrace()));
                        return LogUtils.error(sender, LogUtils.ERROR_UNKNOWN, this.MAIN);
                    }
                } else {
                    return LogUtils.error(sender, "Player not found.", this.MAIN);
                }
            } else {
                return LogUtils.error(sender, USAGE_GIVE, this.MAIN);
            }
        } else {
            return LogUtils.error(sender, LogUtils.ERROR_PERMISSION, this.MAIN);
        }

    }

    private boolean addDrop(CommandSender sender, String[] args) {
        if (sender.hasPermission("hsfishing.rod.add-drop")) {
            if (sender instanceof Player) {
                if (args.length == 6) {
                    // Validate arguments
                /*
                    Args:
                    0 - rod
                    1 - add-drop
                    2 - rod-key
                    3 - key-name
                    4 - weight
                    5 - experience
                 */
                    double weight;
                    double experience;
                    try {
                        weight = Double.parseDouble(args[4]);
                        experience = Double.parseDouble(args[5]);
                    } catch (NumberFormatException ex) {
                        return LogUtils.error(sender, LogUtils.ERROR_NUMBERS, this.MAIN);
                    }

                    final ConfigurationSection CONFIG = this.MAIN.getConfig();
                    Set<String> keys = CONFIG.getKeys(false);
                    if (keys.stream().anyMatch(key -> key.equalsIgnoreCase(args[2]))) {
                        if (!CONFIG.isSet(args[3].toLowerCase())) {
                            ItemStack drop = ((Player) sender).getInventory().getItemInMainHand();
                            if (!drop.getType().equals(Material.AIR)) {
                                try {
                                    CONFIG.set(args[2].toLowerCase() + '.' + "drop-table." + args[3].toLowerCase() +
                                            ".item", ItemSerializer.serialize(drop));
                                    CONFIG.set(args[2].toLowerCase() + '.' + "drop-table." + args[3].toLowerCase() +
                                            ".weight", weight);
                                    CONFIG.set(args[2].toLowerCase() + '.' + "drop-table." + args[3].toLowerCase() +
                                            ".experience", experience);
                                    this.MAIN.saveConfig();

                                    return LogUtils.success(sender, LogUtils.SUCCESS, this.MAIN);
                                } catch (IOException ex) {
                                    this.MAIN.getLogger().severe(Arrays.toString(ex.getStackTrace()));
                                    return LogUtils.error(sender, LogUtils.ERROR_UNKNOWN, this.MAIN);
                                }
                            } else {
                                return LogUtils.error(sender, "You must be holding the drop.", this.MAIN);
                            }
                        } else {
                            return LogUtils.error(sender, "Drop key name already in use.", this.MAIN);
                        }
                    } else {
                        return LogUtils.error(sender, LogUtils.ERROR_WORDS, this.MAIN);
                    }
                } else {
                    return LogUtils.error(sender, USAGE_ROD_ADDDROP, this.MAIN);
                }
            } else {
                return LogUtils.error(sender, LogUtils.PLAYER_ONLY, this.MAIN);
            }
        } else {
            return LogUtils.error(sender, LogUtils.ERROR_PERMISSION, this.MAIN);
        }
    }

    public boolean set(CommandSender sender, String[] args) {
        if (sender.hasPermission("hsfishing.rod.set")) {
            if (sender instanceof Player player) {
                // Check that they are holding a valid rod
                ItemStack heldItem = player.getInventory().getItemInMainHand();
                HSFishingRod rod = null;
                try {
                    rod = new HSFishingRod(this.MAIN, heldItem, player);
                } catch (IOException | IllegalArgumentException ignored) {
                }

                if (rod != null) {
            /*
                0 - rod
                1 - set
                2 - level/perk
                3 - value
            */
                    if (args.length == 4) {
                        Double value = null;
                        try {
                            value = Double.parseDouble(args[3]);
                        } catch (NumberFormatException ignored) {
                            return LogUtils.error(sender, LogUtils.ERROR_NUMBERS, this.MAIN);
                        }

                        if (args[2].equalsIgnoreCase("level")) {
                            rod.setLevel(Math.min(CustomLevelSystem.MAX_LEVEL, Math.max(1, value.intValue())));
                        } else if (args[2].equalsIgnoreCase("skill-points")) {
                            rod.setSkillPoints(Math.max(0, value.intValue()));
                        } else {
                            Perk perk = null;
                            try {
                                perk = Perk.valueOf(args[2].toUpperCase());
                            } catch (IllegalArgumentException ignored) {
                            }

                            switch (perk) {
                                case ITEM_FIND -> rod.setItemLuck(value);
                                case XP_GAIN -> rod.setExperienceMultiplier(value);
                                case FISHING_SPEED -> rod.setFishingSpeed(value);
                                case DOUBLE_DROPS -> rod.setDoubleDrops(value);
                                case DOUBLE_XP -> rod.setDoubleXp(value);
                                default -> {
                                    return LogUtils.error(sender, "/hsfishing rod set <level/perk/skill-points> " +
                                            "<value>", this.MAIN);
                                }
                            }
                        }

                        player.getInventory().setItemInMainHand(rod.getRod());
                        return LogUtils.success(sender, LogUtils.SUCCESS, this.MAIN);
                    } else {
                        return LogUtils.error(sender, "/hsfishing rod set <level/perk> <value>", this.MAIN);
                    }
                } else {
                    return LogUtils.error(sender, LogUtils.ERROR_VALID_ROD, this.MAIN);
                }
            } else {
                return LogUtils.error(sender, LogUtils.PLAYER_ONLY, this.MAIN);
            }
        } else {
            return LogUtils.error(sender, LogUtils.ERROR_PERMISSION, this.MAIN);
        }
    }
}
