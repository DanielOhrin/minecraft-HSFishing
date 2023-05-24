package net.highskiesmc.fishing;

import net.highskiesmc.fishing.events.handlers.PlayerFishHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class HSFishing extends JavaPlugin {

    @Override
    public void onEnable() {

        Bukkit.getPluginManager().registerEvents(new PlayerFishHandler(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
