package net.highskiesmc.fishing.events.handlers;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.island.IslandPrivilege;
import net.highskiesmc.fishing.HSFishing;
import net.highskiesmc.fishing.events.events.IslandFishCaughtEvent;
import net.highskiesmc.fishing.util.HSFishingRod;
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

import java.util.ArrayList;
import java.util.List;

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

            //TODO: Check if they are using a valid fishing rod
            // TODO: set entity to air

            Item itemCaught = (Item) e.getCaught();
            itemCaught.setItemStack(new ItemStack(Material.AIR));
            e.setExpToDrop(0);

            HSFishingRod hsFishingRod;
            try {
                hsFishingRod = new HSFishingRod(this.MAIN,
                        e.getPlayer().getInventory().getItemInMainHand());
            } catch (IllegalArgumentException ex) {
                //TODO: Provide player feedback that they must use a valid fishing rod

                return;
            }

            //TODO: set new drop(s)
            //TODO: get a random drop from the fishingRod object. There will be a method to get a random one based on
            // the weights in config.
            List<ItemStack> drops = new ArrayList<>();

            // Call the custom event
            IslandFishCaughtEvent event = new IslandFishCaughtEvent(player, drops, e.getPlayer().getItemInUse(),
                    island, e.getHook());
            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                e.setCancelled(true);
                return;
            }

            //TODO: Throw drops to player here
        }
    }
}
