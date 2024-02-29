package net.highskiesmc.hsfishing.events.events;

import net.highskiesmc.hsfishing.util.HSFishingRod;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a rod becomes able to unlock the next milestone.
 */
public class RodMilestoneUnlockedEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final HSFishingRod FISHING_ROD;

    public RodMilestoneUnlockedEvent(HSFishingRod rod) {
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
}