package net.highskiesmc.fishing.commands;

import net.highskiesmc.fishing.HSFishing;
import net.highskiesmc.fishing.events.events.RodUpgradedEvent;
import net.highskiesmc.fishing.util.HSFishingRod;
import net.highskiesmc.fishing.util.LogUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;

public class UpgradeRodCommand implements CommandExecutor {
    private static final String ROD_NOT_READY = "Must be holding a rod that is ready to upgrade!";
    private final HSFishing MAIN;

    public UpgradeRodCommand(HSFishing main) {
        this.MAIN = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ItemStack heldItem = player.getInventory().getItemInMainHand();
            HSFishingRod rod = null;
            try {
                rod = new HSFishingRod(this.MAIN, heldItem, player);
            } catch (IOException ignored) {}

            if (rod != null) {
                String oldDisplayName = rod.getDisplayName().split("\\(")[0].trim();
                try {
                    rod.upgradeMilestone();
                } catch (OperationNotSupportedException ignored) {
                    return LogUtils.error(sender, ROD_NOT_READY, this.MAIN);
                }

                RodUpgradedEvent event = new RodUpgradedEvent(oldDisplayName, rod);
                Bukkit.getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    player.getInventory().setItemInMainHand(rod.getRod());
                }
            } else {
                LogUtils.error(sender, ROD_NOT_READY, this.MAIN);
            }
        } else {
            LogUtils.error(sender, LogUtils.PLAYER_ONLY, this.MAIN);
        }

        return false;
    }
}
