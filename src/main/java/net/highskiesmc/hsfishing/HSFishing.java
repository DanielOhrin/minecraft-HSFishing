package net.highskiesmc.hsfishing;

import com.leonardobishop.quests.common.plugin.Quests;
import net.highskiesmc.hsfishing.commands.HSFishingCommand;
import net.highskiesmc.hsfishing.commands.HSFishingTabCompleter;
import net.highskiesmc.hsfishing.commands.UpgradeRodCommand;
import net.highskiesmc.hsfishing.events.handlers.*;
import net.highskiesmc.hsfishing.hooks.quests.FishCatchTaskType;
import net.highskiesmc.hsfishing.util.UpgradeRodGUI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class HSFishing extends JavaPlugin {
    private static boolean usingQuests;
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

        usingQuests = Bukkit.getPluginManager().isPluginEnabled("Quests");
        if (usingQuests) {
            Quests questsPlugin = (Quests) Bukkit.getPluginManager().getPlugin("Quests");
            getLogger().info("Successfully hooked into Quests!");

            if (questsPlugin.getTaskTypeManager().areRegistrationsAccepted()) {
                questsPlugin.getTaskTypeManager().registerTaskType(new FishCatchTaskType());
            }
        }
    }
    public static boolean usingQuests() {
        return usingQuests;
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
