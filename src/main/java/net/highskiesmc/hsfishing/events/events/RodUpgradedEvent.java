package net.highskiesmc.hsfishing.events.events;

import net.highskiesmc.hsfishing.util.HSFishingRod;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a player upgrades their rod to next milestone
 */
public class RodUpgradedEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private final HSFishingRod FISHING_ROD;
    private final String OLD_DISPLAY_NAME;
    private boolean cancellable;

    public RodUpgradedEvent(String oldDisplayName, HSFishingRod rod) {
        this.OLD_DISPLAY_NAME = oldDisplayName;
        this.FISHING_ROD = rod;
    }

    public HSFishingRod getFishingRod() {
        return this.FISHING_ROD;
    }

    public String getOldDisplayName() {
        return this.OLD_DISPLAY_NAME;
    }

    @Override
    public HandlerList getHandlers() {
        return this.HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return this.cancellable;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancellable = b;
    }
}
