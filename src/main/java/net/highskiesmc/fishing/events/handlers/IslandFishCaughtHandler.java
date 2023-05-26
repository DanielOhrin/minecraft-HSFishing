package net.highskiesmc.fishing.events.handlers;

import net.highskiesmc.fishing.events.events.IslandFishCaughtEvent;
import net.highskiesmc.fishing.util.DropEntry;
import net.highskiesmc.fishing.util.HSFishingRod;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IslandFishCaughtHandler implements Listener {
    // Base message that will have its placeholders updated
    private final static String MESSAGE = "{rarity} " + ChatColor.GRAY + ChatColor.BOLD + ">> "
            + ChatColor.RED + "{amount}x {display-name} " + ChatColor.GRAY + '(' + ChatColor.WHITE + '+'
            + ChatColor.DARK_AQUA + "{xp} " + ChatColor.YELLOW + "xp" + ChatColor.GRAY + ')';

    @EventHandler(priority = EventPriority.MONITOR)
    public void onIslandFishCaught(IslandFishCaughtEvent e) {
        HSFishingRod rod = e.getFishingRod();
        List<DropEntry> drops = e.getDroppedItems();
        Player player = rod.getPlayer();
        double expMulti = rod.getExperienceMultiplier();

        for (DropEntry entry : drops) {
            ItemStack drop = entry.getItemStack();
            String displayName = drop.getItemMeta().hasDisplayName()
                    ? drop.getItemMeta().getDisplayName()
                    :
                    Arrays.stream(drop.getType().name().split("-")).map(str -> str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase()).collect(Collectors.joining(" "));
            String xp = new DecimalFormat("#.##").format(entry.getExperience() * expMulti);
            player.sendMessage(
                    MESSAGE
                            .replace("{rarity}", entry.getRarity().getValue())
                            .replace("{amount}", String.valueOf(drop.getAmount()))
                            .replace("{display-name}", displayName)
                            .replace("{xp}", xp)
            );
        }
    }
}
