package net.highskiesmc.fishing.commands;

import net.highskiesmc.fishing.HSFishing;
import net.highskiesmc.fishing.util.HSFishingRod;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class HSFishingCommand implements CommandExecutor {
    private static String USAGE_MAIN = "/hsprogression <rod>";
    private static String USAGE_ROD = "hsprogression <rod> <get>";
    private static String ERROR_PERMISSION = "Insufficient permission.";
    private static String CONSOLE_ONLY = "That command can only be used by a console!";
    private static String PLAYER_ONLY = "That command can only be used by a player!";
    private static String SUCCESS = "Success!";
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
                default:
                    break;
            }
        }

        return usage(sender, USAGE_ROD);
    }

    private boolean getRod(CommandSender sender) {
        if (sender.hasPermission("hsfishing.rod.get")) {
            if (sender instanceof Player) {
                ((Player) sender).getInventory().addItem(new HSFishingRod(this.MAIN).getRod());
                return success(sender, SUCCESS);
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
