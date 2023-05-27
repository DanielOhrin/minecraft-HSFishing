package net.highskiesmc.fishing.commands;

import net.highskiesmc.fishing.HSFishing;
import net.highskiesmc.fishing.util.HSFishingRod;
import net.highskiesmc.fishing.util.ItemSerializer;
import net.highskiesmc.fishing.util.LogUtils;
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
    private final static String USAGE_MAIN = "/hsfishing <rod>";
    private final static String USAGE_ROD = "hsfishing <rod> <get>";
    private final static String USAGE_ROD_ADDDROP = "hsfishing rod addDrop <rod-key> <drop-key> <weight> <experience>";
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
                case "adddrop":
                    return addDrop(sender, args);
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
                    1 - adddrop
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
}
