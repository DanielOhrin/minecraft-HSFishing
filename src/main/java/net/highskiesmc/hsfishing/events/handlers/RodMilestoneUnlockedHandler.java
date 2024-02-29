package net.highskiesmc.hsfishing.events.handlers;

import net.highskiesmc.hsfishing.events.events.RodMilestoneUnlockedEvent;
import net.highskiesmc.hsfishing.util.HSFishingRod;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class RodMilestoneUnlockedHandler implements Listener {
    private static final Sound SOUND = Sound.BLOCK_CHEST_OPEN;
    private static final String PREFIX = ChatColor.DARK_RED + "(" + ChatColor.WHITE + "!" + ChatColor.DARK_RED + ") ";
    private static final String TITLE = ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Milestone Unlocked";
    private static final String SUBTITLE = ChatColor.GOLD.toString() + ChatColor.BOLD + "Type " +
            ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "/upgraderod " +
            ChatColor.GOLD + ChatColor.BOLD + "to upgrade your {rod}";
    private static final String[] MESSAGES = new String[]{
            PREFIX + ChatColor.GRAY + "Fishing rod milestone unlocked!",
            PREFIX + ChatColor.GRAY + "Upgrade with /upgraderod!",
            PREFIX + ChatColor.RED + "This changes its loot pool!"
    };

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRodMilestoneUnlocked(RodMilestoneUnlockedEvent e) {
        // Send player feedback
        HSFishingRod rod = e.getFishingRod();
        Player player = rod.getPlayer();

        player.playSound(player.getLocation(), SOUND, 1, 1);

        String subtitle = SUBTITLE
                .replace("{rod}", rod.getDisplayName().split("\\(")[0]);

        player.sendTitle(
                TITLE,
                subtitle,
                40,
                100,
                40
        );

        for (String message : MESSAGES) {
            player.sendMessage(message);
        }
    }
}
