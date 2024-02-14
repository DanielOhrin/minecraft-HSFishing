package net.highskiesmc.fishing.util;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.lang.module.Configuration;
import java.util.List;

public class ItemLauncher {
    private static final double LAUNCH_SPEED = 0.75; // Adjust this value to control the launch speed
    private static final double MIN_DISTANCE = 0.2; // Minimum distance to consider the item reached the player
    private static final int MAX_TICKS = 100; // Maximum number of ticks before stopping the launching

    public static void launchItems(JavaPlugin plugin, Location startLocation, Player player, List<ItemStack> items,
                                   Particle pParticle) {
        for (ItemStack item : items) {
            Item droppedItem = startLocation.getWorld().dropItem(startLocation, item);
            droppedItem.setOwner(player.getUniqueId()); // Set the owner of the dropped item
            Vector velocity = getLaunchVelocity(player, droppedItem);
            droppedItem.setVelocity(velocity);

            // Start the task to update item position and create particle effects
            new BukkitRunnable() {
                private final Particle particle = pParticle;
                private int ticks = 0;

                @Override
                public void run() {
                    if (droppedItem.isValid() && !droppedItem.isDead() && player.isOnline() && player.getWorld() == droppedItem.getWorld()) {
                        Vector direction = player.getEyeLocation().subtract(droppedItem.getLocation()).toVector();

                        // Check if the item has reached the player's location
                        if (direction.lengthSquared() <= MIN_DISTANCE) {
                            // Stop all movement and prevent another launch
                            droppedItem.setVelocity(new Vector(0, 0, 0));
                            cancel();
                            return;
                        }

                        // Check if the maximum number of ticks has been reached
                        ticks++;
                        if (ticks > MAX_TICKS) {
                            // Maximum number of ticks reached, stop launching
                            cancel();
                            return;
                        }

                        // Update the item's position
                        droppedItem.setVelocity(direction.normalize().multiply(LAUNCH_SPEED));

                        // Create particle effect to connect item to the player
                        Location particleLocation = droppedItem.getLocation().add(0, 0.5, 0);
                        player.getWorld().spawnParticle(this.particle, particleLocation, 1, 0, 0, 0, 0);

                    } else {
                        // Item is no longer valid or dead
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 0, 1);
        }
    }

    private static Vector getLaunchVelocity(Player player, Item item) {
        Vector direction = player.getEyeLocation().subtract(item.getLocation()).toVector();
        return direction.normalize().multiply(LAUNCH_SPEED);
    }
}
