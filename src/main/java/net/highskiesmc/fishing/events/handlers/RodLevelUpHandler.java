package net.highskiesmc.fishing.events.handlers;

import net.highskiesmc.fishing.events.events.RodLevelUpEvent;
import net.highskiesmc.fishing.util.HSFishingRod;
import net.highskiesmc.fishing.util.enums.Perk;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

public class RodLevelUpHandler implements Listener {
    private final static String MESSAGE = ChatColor.YELLOW + "Level Up! {rod}";
    private final static String MESSAGE_PERK =
            ChatColor.WHITE.toString() + ChatColor.BOLD + "Perk: "
                    + ChatColor.YELLOW + "{perk} " +
                    ChatColor.LIGHT_PURPLE + "+{amount}";

    @EventHandler
    public void onRodLevelUp(RodLevelUpEvent e) {
        HSFishingRod rod = e.getFishingRod();
        HashMap<Perk, Double> perkAdded = e.getPerkAdded();
        Player player = rod.getPlayer();
        int newLevel = rod.getLevel();
        String rodMessage = MESSAGE
                .replace("{rod}", rod.getDisplayName())
                .replace(ChatColor.WHITE + ")", " -> " + (newLevel - 1) + ChatColor.WHITE + ')');
        String perkMessage;
        if (perkAdded.isEmpty()) {
            perkMessage = MESSAGE_PERK
                    .replace("{perk}", ChatColor.RED + "None :(")
                    .replace("{amount}", "");
        } else {
            Map.Entry<Perk, Double> perkEntry = perkAdded.entrySet().iterator().next();
            perkMessage = MESSAGE_PERK
                    .replace("{perk}", perkEntry.getKey().getValue())
                    .replace("{amount}", String.valueOf(perkEntry.getValue()));
        }

        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        player.sendMessage(rodMessage);
        player.sendMessage(perkMessage);

    }
}
