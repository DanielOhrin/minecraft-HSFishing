package net.highskiesmc.fishing.events.events;

import net.highskiesmc.fishing.util.HSFishingRod;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a player upgrades their rod to next milestone
 */
public class RodUpgradedEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private final HSFishingRod FISHING_ROD;
    private boolean cancellable;

    public RodUpgradedEvent(HSFishingRod rod) {
        this.FISHING_ROD = rod;
    }

    public HSFishingRod getFishingRod() {
        return this.FISHING_ROD;
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
