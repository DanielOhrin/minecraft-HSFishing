package net.highskiesmc.fishing.events.handlers;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.island.IslandPrivilege;
import com.sun.tools.javac.comp.Check;
import net.highskiesmc.fishing.HSFishing;
import net.highskiesmc.fishing.events.events.IslandFishCaughtEvent;
import net.highskiesmc.fishing.util.DropEntry;
import net.highskiesmc.fishing.util.DropTable;
import net.highskiesmc.fishing.util.HSFishingRod;
import net.highskiesmc.fishing.util.ItemLauncher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerFishHandler implements Listener {
    private final HSFishing MAIN;

    public PlayerFishHandler(HSFishing main) {
        this.MAIN = main;
    }

    @EventHandler
    public void onFishCaught(PlayerFishEvent e) {
        if (e.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)) {
            Island island = SuperiorSkyblockAPI.getIslandAt(e.getHook().getLocation());
            Player player = e.getPlayer();

            if (island == null
                    || !island.hasPermission(SuperiorSkyblockAPI.getPlayer(player.getUniqueId()),
                    IslandPrivilege.getByName("FISH"))) {
                e.setCancelled(true);
                player.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "[!] " +
                        ChatColor.RED + "You can only catch fish on an island where you have permission to!");
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 1);
                return;
            }


            // Clear existing drops
            if (e.getCaught() != null) {
                e.getCaught().remove();
            }
            e.setExpToDrop(0);

            //  Check if they are using a valid fishing rod
            HSFishingRod hsFishingRod;
            try {
                hsFishingRod = new HSFishingRod(this.MAIN,
                        player.getInventory().getItemInMainHand(), player);
            } catch (IllegalArgumentException ex) {
                // Provide player feedback that they must use a valid fishing rod
                e.setCancelled(true);
                player.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "[!] " +
                        ChatColor.RED + "You can only catch fish on an island where you have permission to!");
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 1);
                return;
            } catch (IOException ex) {
                this.MAIN.getLogger().severe(Arrays.toString(ex.getStackTrace()));
                e.setCancelled(true);
                return;
            }

            // Grab item-luck and experience boost from fishing rod
            // Apply item-luck before getting an initial drop
            DropTable dropTable = hsFishingRod.getDropTable();
            double itemLuck = hsFishingRod.getItemLuck();
            dropTable.addWeight(itemLuck);

            List<DropEntry> initialDrops = new ArrayList<>();
            initialDrops.add(dropTable.getRandomDrop());

            // Call the custom event
            IslandFishCaughtEvent event = new IslandFishCaughtEvent(player, initialDrops, hsFishingRod,
                    island, e.getHook());
            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                e.setCancelled(true);
                return;
            }

            // Calculate total experience gain
            double expMulti = hsFishingRod.getExperienceMultiplier();
            List<DropEntry> drops = event.getDroppedItems();

            double totalExp = 0;
            for (DropEntry drop : drops) {
                totalExp += drop.getExperience();
            }

            // Apply the multiplier
            totalExp = Double.parseDouble(new DecimalFormat("#.##").format(totalExp * expMulti));

            // Add exp to rod
            hsFishingRod.addExperience(totalExp);
            // Add to total caught amount
            hsFishingRod.addCaughtItems(drops.size());

            // Throw drops to player
            ItemLauncher.launchItems(
                    this.MAIN,
                    event.getHook().getLocation(),
                    player,
                    drops.stream().map(DropEntry::getItemStack).collect(Collectors.toList()),
                    hsFishingRod.getRodConfig()
            );

            // Replace their rod with the updated one
            player.getInventory().setItemInMainHand(hsFishingRod.getRod());
        }
    }
}
