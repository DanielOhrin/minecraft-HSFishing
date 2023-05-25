package net.highskiesmc.fishing.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class HSFishingTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> result = new ArrayList<>();

        switch (args.length) {
            case 1:
                if (sender.hasPermission("hsfishing.tab.rod")) {
                    result.add("rod");
                }
                if (sender.hasPermission("hsfishing.tab.reload")) {
                    result.add("reload");
                }
            case 2:
                if (args[0].equalsIgnoreCase("rod")) {
                    result.add("get");
                }
            default:
                break;
        }

        return result;
    }
}
