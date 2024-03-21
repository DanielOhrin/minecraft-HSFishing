package net.highskiesmc.hsfishing.events.handlers;

import net.highskiesmc.hsfishing.HSFishing;
import net.highskiesmc.hsfishing.events.events.FishCaughtEvent;
import net.highskiesmc.hsfishing.hooks.quests.FishCatchTaskType;
import net.highskiesmc.hsfishing.util.DropEntry;
import net.highskiesmc.hsfishing.util.HSFishingRod;
import net.highskiesmc.hsfishing.util.HologramUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class FishCaughtHandler implements Listener {
    private final HSFishing main;

    public FishCaughtHandler(HSFishing main) {
        this.main = main;
    }

    // Base message that will have its placeholders updated
    private final static String MESSAGE = "{rarity} " + ChatColor.GRAY + ChatColor.BOLD + ">> "
            + ChatColor.RED + "{amount}x {display-name} " + ChatColor.GRAY + '(' + ChatColor.WHITE + '+'
            + ChatColor.DARK_AQUA + "{xp} " + ChatColor.YELLOW + "xp" + ChatColor.GRAY + ')';


    @EventHandler(priority = EventPriority.HIGHEST)
    public void applyExtraPerks(FishCaughtEvent e) {
        if (e.getFishingRod() == null) {
            return;
        }

        final HSFishingRod ROD = e.getFishingRod();

        List<DropEntry> drops = e.getDroppedItems();

        if (ROD.getDoubleDrops() > 0) {
            double rng = new Random().nextDouble();
            if (rng <= ROD.getDoubleDrops() / 100D) {
                for (DropEntry drop : drops) {
                    drop.setAmount(drop.getAmount() * 2);
                }

                HologramUtils.spawnAnimated(this.main, e.getHook().getLocation().subtract(0, 1.5, 0),
                        ChatColor.YELLOW.toString() + ChatColor.BOLD + "DOUBLE DROPS",
                        0, 1);
            }

        }

        if (ROD.getDoubleXp() > 0) {
            double rng = new Random().nextDouble();
            if (rng <= ROD.getDoubleXp() / 100D) {
                for (DropEntry drop : drops) {
                    drop.setExperience(drop.getExperience() * 2);
                }

                HologramUtils.spawnAnimated(this.main, e.getHook().getLocation().subtract(0, 1.5, 0),
                        ChatColor.YELLOW.toString() + ChatColor.BOLD + "DOUBLE XP",
                        0, 1);
            }
        }


        e.setDroppedItems(drops);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onFishCaught(FishCaughtEvent e) {
        HSFishingRod rod = e.getFishingRod();
        List<DropEntry> drops = e.getDroppedItems();
        Player player = e.getPlayer();

        for (DropEntry entry : drops) {
            ItemStack drop = entry.getItemStack();

            if (HSFishing.usingQuests()) {
                updateQuests(e, drop);
            }

            if (rod != null) {
                sendDebug(entry, drop, rod, player);
            }

        }
    }

    private void updateQuests(FishCaughtEvent e, ItemStack drop) {
        ItemMeta meta = drop.getItemMeta();

        String fishId = drop.getType().toString();
        if (meta != null) {
            fishId = meta.getDisplayName();
        }

        for (int i = 0; i < drop.getAmount(); i++) {
            FishCatchTaskType.updateQuests(e, fishId, main);
        }
    }

    private void sendDebug(DropEntry entry, ItemStack drop, HSFishingRod rod, Player player) {
        double expMulti = rod.getExperienceMultiplier();

        String displayName = drop.getItemMeta().hasDisplayName()
                ? drop.getItemMeta().getDisplayName()
                :
                Arrays.stream(drop.getType().name().split("_")).map(str -> str.substring(0,
                        1).toUpperCase() + str.substring(1).toLowerCase()).collect(Collectors.joining(" "));
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
