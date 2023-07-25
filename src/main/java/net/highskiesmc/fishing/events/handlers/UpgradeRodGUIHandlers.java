package net.highskiesmc.fishing.events.handlers;

import net.highskiesmc.fishing.HSFishing;
import net.highskiesmc.fishing.events.events.RodUpgradedEvent;
import net.highskiesmc.fishing.inventories.RodUpgradesGUI;
import net.highskiesmc.fishing.util.HSFishingRod;
import net.highskiesmc.fishing.util.UpgradeRodGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class UpgradeRodGUIHandlers implements Listener {
    private final HashMap<UUID, UpgradeRodGUI> OPEN_UPGRADEROD_GUIS;

    public UpgradeRodGUIHandlers(HashMap<UUID, UpgradeRodGUI> openGuis) {
        this.OPEN_UPGRADEROD_GUIS = openGuis;
    }

    @EventHandler
    public void onGUIClose(InventoryCloseEvent e) {
        this.OPEN_UPGRADEROD_GUIS.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        this.OPEN_UPGRADEROD_GUIS.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onGUIClick(InventoryClickEvent e) {
        UpgradeRodGUI GUI = this.OPEN_UPGRADEROD_GUIS.getOrDefault(e.getWhoClicked().getUniqueId(), null);

        if (GUI != null) {
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            HSFishingRod oldRod = GUI.getOldRod();
            HSFishingRod newRod = GUI.getNewRod();
            boolean rodIsReady = GUI.isReady();

            if (rodIsReady) {
                // Confirmation GUI
                String oldDisplayName = oldRod.getDisplayName().split("\\(")[0].trim();

                switch (e.getRawSlot()) {
                    case 12, 13, 14 -> {
                        RodUpgradedEvent event = new RodUpgradedEvent(oldDisplayName, newRod);
                        Bukkit.getPluginManager().callEvent(event);
                        if (!event.isCancelled()) {
                            player.getInventory().setItemInMainHand(newRod.getRod());
                        }
                        e.getView().close();
                        this.OPEN_UPGRADEROD_GUIS.remove(player.getUniqueId());
                    }
                    default -> e.setCancelled(true);
                }
            }
        }
    }


    // Rod Upgrade Handlers (Fishing Speed, Xp Gain, etc.)
    @EventHandler
    public void onSwapHSFishingRodToOffhand(PlayerSwapHandItemsEvent e) {
        HSFishingRod rod;
        Player player = e.getPlayer();

        try {
            rod = new HSFishingRod(HSFishing.getPlugin(HSFishing.class), e.getOffHandItem(), player);
        } catch (IllegalArgumentException | IOException ignore) {
            return;
        }

        e.setCancelled(true);
        RodUpgradesGUI gui = new RodUpgradesGUI(rod);
        player.openInventory(gui.getInventory());
    }
}
