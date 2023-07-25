package net.highskiesmc.fishing.commands;

import net.highskiesmc.fishing.HSFishing;
import net.highskiesmc.fishing.util.HSFishingRod;
import net.highskiesmc.fishing.util.LogUtils;
import net.highskiesmc.fishing.util.UpgradeRodGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class UpgradeRodCommand implements CommandExecutor {
    private static final String ERROR_ROD_MAX_MILESTONE = "Held rod has already reached the max milestone!";
    private final HSFishing MAIN;
    private final HashMap<UUID, UpgradeRodGUI> OPEN_UPGRADEROD_GUIS;

    public UpgradeRodCommand(HSFishing main, HashMap<UUID, UpgradeRodGUI> openGUIs) {
        this.MAIN = main;
        this.OPEN_UPGRADEROD_GUIS = openGUIs;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ItemStack heldItem = player.getInventory().getItemInMainHand();
            HSFishingRod oldRod = null;
            HSFishingRod newRod = null;
            try {
                oldRod = new HSFishingRod(this.MAIN, heldItem, player);
                newRod = new HSFishingRod(this.MAIN, heldItem, player);
            } catch (IOException | IllegalArgumentException ignored) {}

            if (oldRod != null && newRod != null) {
                boolean rodIsReady = false;
                try {
                    newRod.upgradeMilestone(false);
                    rodIsReady = true;
                } catch (IllegalStateException ignored) {
                    try {
                        newRod.upgradeMilestone(true);
                    } catch (IllegalStateException ignore) {}
                }

                // Check if the rod is max level
                if (newRod.getLevel() == -1) {
                    return LogUtils.error(sender, ERROR_ROD_MAX_MILESTONE, this.MAIN);
                }

                // Create the Inventory (GUI)
                Inventory inv = Bukkit.createInventory(player, InventoryType.CHEST,
                        ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Upgrade Rod");

                // Set the clickable slots
                int[] clickableSlots = new int[] {12, 13, 14};
                ItemStack clickableItem = null;
                if (rodIsReady) {
                    clickableItem = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                    ItemMeta meta = clickableItem.getItemMeta();
                    meta.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "CONFIRM");
                    clickableItem.setItemMeta(meta);
                } else {
                    clickableItem = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                    ItemMeta meta = clickableItem.getItemMeta();
                    meta.setDisplayName(ChatColor.RED.toString() + ChatColor.BOLD + "NOT READY");
                    clickableItem.setItemMeta(meta);
                }
                for(int slot : clickableSlots) {
                    inv.setItem(slot, clickableItem);
                }
                // Set the old and new rod slots
                inv.setItem(11, oldRod.getRod());
                inv.setItem(15, newRod.getRod());

                // Fill the rest of the inventory with a filler item
                ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                ItemMeta meta = filler.getItemMeta();
                meta.setDisplayName(" ");
                filler.setItemMeta(meta);
                for (int i = 0; i < inv.getSize(); i++) {
                    if (inv.getItem(i) == null) {
                        inv.setItem(i, filler);
                    }
                }
                // Add the GUI to the OPEN guis HashMap
                UpgradeRodGUI upgradeGUI = new UpgradeRodGUI(oldRod, newRod, inv, rodIsReady);
                this.OPEN_UPGRADEROD_GUIS.put(player.getUniqueId(), upgradeGUI);

                // Open the inventory
                player.openInventory(inv);
            } else {
                LogUtils.error(sender, LogUtils.ERROR_VALID_ROD, this.MAIN);
            }
        } else {
            LogUtils.error(sender, LogUtils.PLAYER_ONLY, this.MAIN);
        }

        return false;
    }
}
