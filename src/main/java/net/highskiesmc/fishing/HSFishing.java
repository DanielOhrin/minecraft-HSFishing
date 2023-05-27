package net.highskiesmc.fishing;

import net.highskiesmc.fishing.commands.HSFishingCommand;
import net.highskiesmc.fishing.commands.HSFishingTabCompleter;
import net.highskiesmc.fishing.commands.UpgradeRodCommand;
import net.highskiesmc.fishing.events.handlers.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class HSFishing extends JavaPlugin {

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        getCommand("hsfishing").setExecutor(new HSFishingCommand(this));
        getCommand("hsfishing").setTabCompleter(new HSFishingTabCompleter(this));
        getCommand("upgraderod").setExecutor(new UpgradeRodCommand(this));

        Bukkit.getPluginManager().registerEvents(new PlayerFishHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new FishCaughtHandler(), this);
        Bukkit.getPluginManager().registerEvents(new RodLevelUpHandler(), this);
        Bukkit.getPluginManager().registerEvents(new RodMilestoneUnlockedHandler(), this);
        Bukkit.getPluginManager().registerEvents(new RodUpgradedHandler(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
