package net.highskiesmc.hsfishing.events.handlers;

import net.highskiesmc.hsfishing.events.events.RodUpgradedEvent;
import net.highskiesmc.hsfishing.util.HSFishingRod;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class RodUpgradedHandler implements Listener {
    private static final Sound SOUND = Sound.BLOCK_ANVIL_USE;
    private static final String TITLE = ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Rod Upgraded";
    private static final String SUBTITLE = "{old-rod}-> {new-rod}";
    @EventHandler(priority = EventPriority.MONITOR)
    public void onRodUpgrade(RodUpgradedEvent e) {
        // Send player feedback
        HSFishingRod rod = e.getFishingRod();
        Player player = rod.getPlayer();

        player.playSound(player.getLocation(), SOUND, 1, 1);

        String subtitle = SUBTITLE
                .replace("{old-rod}", e.getOldDisplayName())
                .replace("{new-rod}", rod.getDisplayName().split("\\(")[0]);

        player.sendTitle(
                TITLE,
                subtitle,
                40,
                100,
                40
        );
    }
}
