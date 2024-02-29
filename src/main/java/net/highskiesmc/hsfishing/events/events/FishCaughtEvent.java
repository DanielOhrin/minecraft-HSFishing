package net.highskiesmc.hsfishing.events.events;

import net.highskiesmc.hsfishing.util.DropEntry;
import net.highskiesmc.hsfishing.util.HSFishingRod;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

public class FishCaughtEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled;
    private final Player PLAYER;
    private final HSFishingRod FISHING_ROD;
    private List<DropEntry> DROPPED_ITEMS;
    private final FishHook HOOK;

    public FishCaughtEvent(Player player, List<DropEntry> droppedItems, HSFishingRod fishingRod, FishHook fishHook) {
        this.cancelled = false;
        this.PLAYER = player;
        this.DROPPED_ITEMS = droppedItems;
        this.FISHING_ROD = fishingRod;
        this.HOOK = fishHook;
    }

    public Player getPlayer() {
        return this.PLAYER;
    }

    @Nullable
    public HSFishingRod getFishingRod() {
        return this.FISHING_ROD;
    }

    public FishHook getHook() {
        return this.HOOK;
    }

    public List<DropEntry> getDroppedItems() {
        return this.DROPPED_ITEMS;
    }

    public void setDroppedItems(List<DropEntry> droppedItems) {
        this.DROPPED_ITEMS = droppedItems;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}


