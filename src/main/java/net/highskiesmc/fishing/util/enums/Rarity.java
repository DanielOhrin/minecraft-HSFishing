package net.highskiesmc.fishing.util.enums;

import org.bukkit.ChatColor;

public enum Rarity {
    COMMON(ChatColor.GRAY),
    UNCOMMON(ChatColor.YELLOW),
    RARE(ChatColor.GREEN),
    EPIC(ChatColor.AQUA),
    LEGENDARY(ChatColor.BLUE),
    UNIQUE(ChatColor.LIGHT_PURPLE),
    SPECIAL(ChatColor.LIGHT_PURPLE);
    private final ChatColor COLOR;
    Rarity(ChatColor chatColor) {
        this.COLOR = chatColor;
    }
    public String getValue() {
        return this.COLOR.toString() + ChatColor.BOLD + this.name();
    }
}
