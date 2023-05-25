package net.highskiesmc.fishing.events.events;

import com.bgsoftware.superiorskyblock.api.island.Island;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class IslandFishCaughtEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled;
    private final Player PLAYER;
    private final ItemStack FISHING_ROD;
    private List<ItemStack> DROPPED_ITEMS;
    private final Island ISLAND;
    private final FishHook HOOK;

    public IslandFishCaughtEvent(Player player, List<ItemStack> droppedItems, ItemStack itemInHand, Island island,
                                 FishHook fishHook) {
        this.cancelled = false;
        this.PLAYER = player;
        this.DROPPED_ITEMS = droppedItems;
        this.FISHING_ROD = itemInHand;
        this.ISLAND = island;
        this.HOOK = fishHook;
    }

    public Player getPlayer() {
        return this.PLAYER;
    }

    public ItemStack getFishingRod() {
        return this.FISHING_ROD;
    }
    public FishHook getHook() {
        return this.HOOK;
    }

    public Island getIsland() {
        return this.ISLAND;
    }

    public List<ItemStack> getDroppedItems() {
        return this.DROPPED_ITEMS;
    }

    public void setDroppedItems(List<ItemStack> droppedItems) {
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


