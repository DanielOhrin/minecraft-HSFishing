package net.highskiesmc.fishing.commands;

import net.highskiesmc.fishing.HSFishing;
import net.highskiesmc.fishing.util.HSFishingRod;
import net.highskiesmc.fishing.util.ItemSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

public class HSFishingCommand implements CommandExecutor {
    private final static String USAGE_MAIN = "/hsprogression <rod>";
    private final static String USAGE_ROD = "hsprogression <rod> <get>";
    private final static String USAGE_ROD_ADDDROP = "hsprogression rod addDrop <rod-key> <drop-key> <weight> " +
            "<experience>";
    private final static String ERROR_PERMISSION = "Insufficient permission.";
    private final static String ERROR_UNKNOWN = "An unknown error occurred.";
    private final static String ERROR_NUMBERS = "Invalid number inputs received.";
    private final static String ERROR_WORDS = "Invalid word arguments given.";
    private final static String CONSOLE_ONLY = "That command can only be used by a console!";
    private final static String PLAYER_ONLY = "That command can only be used by a player!";
    private final static String SUCCESS = "Success!";
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
        return false;
    }

    private boolean reload(CommandSender sender) {
        if (sender.hasPermission("hsfishing.reload")) {
            this.MAIN.reloadConfig();
            return success(sender, SUCCESS);
        } else {
            return error(sender, ERROR_PERMISSION);
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

        return usage(sender, USAGE_ROD);
    }

    private boolean getRod(CommandSender sender) {
        if (sender.hasPermission("hsfishing.rod.get")) {
            if (sender instanceof Player) {
                try {
                    ((Player) sender).getInventory().addItem(new HSFishingRod(this.MAIN).getRod());
                    return success(sender, SUCCESS);
                } catch (IOException ex) {
                    this.MAIN.getLogger().severe(Arrays.toString(ex.getStackTrace()));
                    return error(sender, ERROR_UNKNOWN);
                }
            } else {
                return error(sender, PLAYER_ONLY);
            }
        } else {
            return error(sender, ERROR_PERMISSION);
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
                        return error(sender, ERROR_NUMBERS);
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

                                    return success(sender, SUCCESS);
                                } catch (IOException ex) {
                                    this.MAIN.getLogger().severe(Arrays.toString(ex.getStackTrace()));
                                    return error(sender, ERROR_UNKNOWN);
                                }
                            } else {
                                return error(sender, "You must be holding the drop.");
                            }
                        } else {
                            return error(sender, "Drop key name already in use.");
                        }
                    } else {
                        return error(sender, ERROR_WORDS);
                    }
                } else {
                    return error(sender, USAGE_ROD_ADDDROP);
                }
            } else {
                return error(sender, PLAYER_ONLY);
            }
        } else {
            return error(sender, ERROR_PERMISSION);
        }
    }

    private boolean success(CommandSender sender, String success) {
        if (sender instanceof Player) {
            sender.sendMessage(ChatColor.GREEN + success);
        } else if (sender instanceof ConsoleCommandSender) {
            this.MAIN.getLogger().finest(success);
        }

        return true;
    }

    private boolean error(CommandSender sender, String error) {
        if (sender instanceof Player) {
            sender.sendMessage(ChatColor.RED + error);
        } else if (sender instanceof ConsoleCommandSender) {
            this.MAIN.getLogger().warning(error);
        }

        return false;
    }

    private boolean usage(CommandSender sender, String usage) {
        if (sender instanceof Player) {
            sender.sendMessage(ChatColor.RED + "Incorrect usage! " + usage);
        } else if (sender instanceof ConsoleCommandSender) {
            this.MAIN.getLogger().warning("Incorrect usage! " + usage);
        }

        return false;
    }
}
