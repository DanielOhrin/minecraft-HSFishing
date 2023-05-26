package net.highskiesmc.fishing.commands;

import net.highskiesmc.fishing.HSFishing;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class HSFishingTabCompleter implements TabCompleter {
    private final HSFishing MAIN;

    public HSFishingTabCompleter(HSFishing main) {
        this.MAIN = main;
    }

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
                break;
            case 2:
                if (args[0].equalsIgnoreCase("rod")) {
                    if (sender.hasPermission("hsfishing.tab.rod")) {
                        result.add("get");
                        result.add("addDrop");
                    }
                }
                break;
            case 3:
                if (args[0].equalsIgnoreCase("rod")) {
                    if (args[1].equalsIgnoreCase("adddrop")) {
                        if (sender.hasPermission("hsfishing.rod.add-drop")) {
                            result.addAll(this.MAIN.getConfig().getKeys(false));
                        }
                    }
                }
                break;
            default:
                break;
        }

        return result;
    }
}
