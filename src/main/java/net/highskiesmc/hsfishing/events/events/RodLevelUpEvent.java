package net.highskiesmc.hsfishing.events.events;

import net.highskiesmc.hsfishing.util.HSFishingRod;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RodLevelUpEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final HSFishingRod FISHING_ROD;
    private final int SKILL_POINTS;

    public RodLevelUpEvent(HSFishingRod rod, int skillPoints) {
        this.FISHING_ROD = rod;
        this.SKILL_POINTS = skillPoints;
    }

    public HSFishingRod getFishingRod() {
        return this.FISHING_ROD;
    }

    public int getSkillPoints() {
        return this.SKILL_POINTS;
    }

    @Override
    public HandlerList getHandlers() {
        return this.HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
