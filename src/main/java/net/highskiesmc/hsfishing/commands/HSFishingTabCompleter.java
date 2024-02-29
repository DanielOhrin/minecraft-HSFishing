package net.highskiesmc.hsfishing.commands;

import net.highskiesmc.hsfishing.HSFishing;
import net.highskiesmc.hsfishing.util.enums.Perk;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
                        result.add("give");
                        result.add("add-drop");
                        result.add("set");
                    }
                }
                break;
            case 3:
                if (args[0].equalsIgnoreCase("rod")) {
                    if (args[1].equalsIgnoreCase("add-drop")) {
                        if (sender.hasPermission("hsfishing.rod.add-drop")) {
                            result.addAll(this.MAIN.getConfig().getKeys(false));
                        }
                    } else if (args[1].equalsIgnoreCase("set")) {
                        result.add("level");
                        result.add("skill-points");
                        result.addAll(Arrays.stream(Perk.values()).map(Enum::name).collect(Collectors.toList()));
                    } else if (args[1].equalsIgnoreCase("give")) {
                        result.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(x -> x.toLowerCase().contains(args[2].toLowerCase())).collect(Collectors.toList()));
                    }
                }
                break;
            case 4:
                if (args[0].equalsIgnoreCase("rod")) {
                    if (args[1].equalsIgnoreCase("set")) {
                        result.add("<value>");
                    } else if (args[1].equalsIgnoreCase("give")) {
                        result.add("<level>");
                    }
                }
            default:
                break;
        }

        return result;
    }
}
