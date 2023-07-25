package net.highskiesmc.fishing;

import net.highskiesmc.fishing.commands.HSFishingCommand;
import net.highskiesmc.fishing.commands.HSFishingTabCompleter;
import net.highskiesmc.fishing.commands.UpgradeRodCommand;
import net.highskiesmc.fishing.events.handlers.*;
import net.highskiesmc.fishing.util.UpgradeRodGUI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class HSFishing extends JavaPlugin {
    private final HashMap<UUID, UpgradeRodGUI> OPEN_UPGRADEROD_GUIS = new HashMap<>();

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        getCommand("hsfishing").setExecutor(new HSFishingCommand(this));
        getCommand("hsfishing").setTabCompleter(new HSFishingTabCompleter(this));
        getCommand("upgraderod").setExecutor(new UpgradeRodCommand(this, this.OPEN_UPGRADEROD_GUIS));

        Bukkit.getPluginManager().registerEvents(new InventoryHandler(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerFishHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new FishCaughtHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new RodLevelUpHandler(), this);
        Bukkit.getPluginManager().registerEvents(new RodMilestoneUnlockedHandler(), this);
        Bukkit.getPluginManager().registerEvents(new RodUpgradedHandler(), this);
        Bukkit.getPluginManager().registerEvents(new UpgradeRodGUIHandlers(this.OPEN_UPGRADEROD_GUIS), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
