package net.highskiesmc.fishing.util;

import net.highskiesmc.fishing.HSFishing;
import org.bukkit.inventory.Inventory;

public class UpgradeRodGUI {
    private final Inventory INVENTORY;
    private final boolean IS_READY;
    private final HSFishingRod OLD_FISHING_ROD;
    private final HSFishingRod NEW_FISHING_ROD;

    public UpgradeRodGUI(HSFishingRod oldRod, HSFishingRod newRod, Inventory inventory, boolean isReady) {
        this.OLD_FISHING_ROD = oldRod;
        this.NEW_FISHING_ROD = newRod;
        this.INVENTORY = inventory;
        this.IS_READY = isReady;
    }

    public HSFishingRod getOldRod() {
        return this.OLD_FISHING_ROD;
    }

    public HSFishingRod getNewRod() {
        return this.NEW_FISHING_ROD;
    }

    public Inventory getInventory() {
        return this.INVENTORY;
    }

    public boolean isReady() {
        return this.IS_READY;
    }
}
