package net.highskiesmc.fishing.util;

import net.highskiesmc.fishing.HSFishing;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class LogUtils {
    public final static String ERROR_PERMISSION = "Insufficient permission.";
    public final static String ERROR_UNKNOWN = "An unknown error occurred.";
    public final static String ERROR_NUMBERS = "Invalid number inputs received.";
    public final static String ERROR_WORDS = "Invalid word arguments given.";
    public final static String CONSOLE_ONLY = "That command can only be used by a console!";
    public final static String PLAYER_ONLY = "That command can only be used by a player!";
    public final static String SUCCESS = "Success!";

    public static boolean success(CommandSender sender, String success, HSFishing main) {
        if (sender instanceof Player) {
            sender.sendMessage(ChatColor.GREEN + success);
        } else if (sender instanceof ConsoleCommandSender) {
            main.getLogger().finest(success);
        }

        return true;
    }

    public static boolean error(CommandSender sender, String error, HSFishing main) {
        if (sender instanceof Player) {
            sender.sendMessage(ChatColor.RED + error);
        } else if (sender instanceof ConsoleCommandSender) {
            main.getLogger().warning(error);
        }

        return false;
    }

    public static boolean usage(CommandSender sender, String usage, HSFishing main) {
        if (sender instanceof Player) {
            sender.sendMessage(ChatColor.RED + "Incorrect usage! " + usage);
        } else if (sender instanceof ConsoleCommandSender) {
            main.getLogger().warning("Incorrect usage! " + usage);
        }

        return false;
    }
}