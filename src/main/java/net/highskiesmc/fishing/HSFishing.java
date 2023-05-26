package net.highskiesmc.fishing;

import net.highskiesmc.fishing.commands.HSFishingCommand;
import net.highskiesmc.fishing.commands.HSFishingTabCompleter;
import net.highskiesmc.fishing.events.handlers.IslandFishCaughtHandler;
import net.highskiesmc.fishing.events.handlers.PlayerFishHandler;
import net.highskiesmc.fishing.events.handlers.RodLevelUpHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class HSFishing extends JavaPlugin {

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        getCommand("hsfishing").setExecutor(new HSFishingCommand(this));
        getCommand("hsfishing").setTabCompleter(new HSFishingTabCompleter(this));

        Bukkit.getPluginManager().registerEvents(new PlayerFishHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new IslandFishCaughtHandler(), this);
        Bukkit.getPluginManager().registerEvents(new RodLevelUpHandler(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
