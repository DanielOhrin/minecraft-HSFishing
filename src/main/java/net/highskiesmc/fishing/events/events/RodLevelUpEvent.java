package net.highskiesmc.fishing.events.events;

import net.highskiesmc.fishing.util.HSFishingRod;
import net.highskiesmc.fishing.util.enums.Perk;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.HashMap;

public class RodLevelUpEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final HSFishingRod FISHING_ROD;
    private final HashMap<Perk, Double> PERK_ADDED;

    public RodLevelUpEvent(HSFishingRod rod, HashMap<Perk, Double> perk) {
        this.FISHING_ROD = rod;
        this.PERK_ADDED = perk;
    }

    public HSFishingRod getFishingRod() {
        return this.FISHING_ROD;
    }

    public HashMap<Perk, Double> getPerkAdded() {
        return this.PERK_ADDED;
    }

    @Override
    public HandlerList getHandlers() {
        return this.HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
