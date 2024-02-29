package net.highskiesmc.hsfishing.events.handlers;

import net.highskiesmc.hsfishing.events.events.RodLevelUpEvent;
import net.highskiesmc.hsfishing.util.HSFishingRod;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RodLevelUpHandler implements Listener {
    private final static String MESSAGE = ChatColor.YELLOW + "Level Up! {rod}";
    public final static String MESSAGE_SKILL_POINTS = ChatColor.LIGHT_PURPLE + "+{amount} " + ChatColor.YELLOW +
            "Skill Points";

    @EventHandler
    public void onRodLevelUp(RodLevelUpEvent e) {
        HSFishingRod rod = e.getFishingRod();
        Player player = rod.getPlayer();
        int newLevel = rod.getLevel();

        String rodMessage = MESSAGE
                .replace("{rod}", rod.getDisplayName())
                .replaceAll("(?<!§)" + newLevel, (newLevel - 1) + " -> " + newLevel);


        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        player.sendMessage(rodMessage);

        int skillPointsGained = e.getSkillPoints();
        if (skillPointsGained > 0)
            player.sendMessage(MESSAGE_SKILL_POINTS.replace("{amount}", String.valueOf(skillPointsGained)));
    }
}
