package net.highskiesmc.fishing.commands;

import net.highskiesmc.fishing.HSFishing;
import net.highskiesmc.fishing.events.handlers.RodLevelUpHandler;
import net.highskiesmc.fishing.util.CustomLevelSystem;
import net.highskiesmc.fishing.util.HSFishingRod;
import net.highskiesmc.fishing.util.ItemSerializer;
import net.highskiesmc.fishing.util.LogUtils;
import net.highskiesmc.fishing.util.enums.Perk;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HSFishingCommand implements CommandExecutor {
    private final static String USAGE_MAIN = "/hsfishing <rod>";
    private final static String USAGE_ROD = "hsfishing <rod> <get>";
    private final static String USAGE_ROD_ADDDROP = "hsfishing rod add-drop <rod-key> <drop-key> <weight> <experience>";
    private final HSFishing MAIN;

    public HSFishingCommand(HSFishing main) {
        this.MAIN = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "rod":
                    return this.rod(sender, args);
                case "reload":
                    return this.reload(sender);
                default:
                    break;
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
                case "get":
                    return getRod(sender);
                case "add-drop":
                    return addDrop(sender, args);
                case "set":
                    return set(sender, args);
                case "add-perk":
                    return addPerk(sender, args);
                default:
                    break;
            }
        }

        return LogUtils.usage(sender, USAGE_ROD, this.MAIN);
    }

    private boolean getRod(CommandSender sender) {
        if (sender.hasPermission("hsfishing.rod.get")) {
            if (sender instanceof Player) {
                try {
                    ((Player) sender).getInventory().addItem(new HSFishingRod(this.MAIN, (Player) sender).getRod());
                    return LogUtils.success(sender, LogUtils.SUCCESS, this.MAIN);
                } catch (IOException ex) {
                    this.MAIN.getLogger().severe(Arrays.toString(ex.getStackTrace()));
                    return LogUtils.error(sender, LogUtils.ERROR_UNKNOWN, this.MAIN);
                }
            } else {
                return LogUtils.error(sender, LogUtils.PLAYER_ONLY, this.MAIN);
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
            if (sender instanceof Player) {
                // Check that they are holding a valid rod
                Player player = (Player) sender;
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
                        } else {
                            Perk perk = null;
                            try {
                                perk = Perk.valueOf(args[2].toUpperCase());
                            } catch (IllegalArgumentException ignored) {
                            }

                            switch (perk) {
                                case ITEM_FIND:
                                    rod.setItemLuck(value);
                                    break;
                                case EXPERIENCE_MULTIPLIER:
                                    rod.setExperienceMultiplier(value);
                                    break;
                                default:
                                    return LogUtils.error(sender, "/hsfishing rod set <level/perk> <value>",
                                            this.MAIN);
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

    private boolean addPerk(CommandSender sender, String[] args) {
        if (sender.hasPermission("hsfishing.rod.add-perk")) {
            if (sender instanceof Player) {
                // Check that they are holding a valid rod
                Player player = (Player) sender;
                ItemStack heldItem = player.getInventory().getItemInMainHand();
                HSFishingRod rod = null;
                try {
                    rod = new HSFishingRod(this.MAIN, heldItem, player);
                } catch (IOException | IllegalArgumentException ignored) {
                }

                if (rod != null) {
                    HashMap<Perk, Double> perkAdded = rod.addRandomPerk();

                    String perkMessage;
                    if (perkAdded.isEmpty()) {
                        perkMessage = RodLevelUpHandler.MESSAGE_PERK
                                .replace("{perk}", ChatColor.RED + "None :(")
                                .replace("+{amount}", "");
                    } else {
                        Map.Entry<Perk, Double> perkEntry = perkAdded.entrySet().iterator().next();
                        perkMessage = RodLevelUpHandler.MESSAGE_PERK
                                .replace("{perk}", perkEntry.getKey().getValue())
                                .replace("{amount}", String.valueOf(perkEntry.getValue()));
                    }

                    player.getInventory().setItemInMainHand(rod.getRod());
                    player.sendMessage(perkMessage);
                    return LogUtils.success(sender, LogUtils.SUCCESS, this.MAIN);
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
