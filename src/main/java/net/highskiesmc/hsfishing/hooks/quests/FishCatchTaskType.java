package net.highskiesmc.hsfishing.hooks.quests;

import com.leonardobishop.quests.bukkit.tasktype.BukkitTaskType;
import com.leonardobishop.quests.common.player.QPlayer;
import com.leonardobishop.quests.common.player.questprogressfile.QuestProgressFile;
import com.leonardobishop.quests.common.player.questprogressfile.TaskProgress;
import com.leonardobishop.quests.common.plugin.Quests;
import com.leonardobishop.quests.common.quest.Quest;
import com.leonardobishop.quests.common.quest.Task;
import net.highskiesmc.hsfishing.HSFishing;
import net.highskiesmc.hsfishing.events.events.FishCaughtEvent;

import java.util.Arrays;
import java.util.List;

public class FishCatchTaskType extends BukkitTaskType {
    public FishCatchTaskType() {
        super("fishcaught", null, "Fish a certain amount of items.");
    }

    public static void updateQuests(FishCaughtEvent e, String fishId, HSFishing main, Quests q) {
        // Iterate over every quest registered to the task type
        QPlayer qPlayer = q.getPlayerManager().getPlayer(e.getPlayer().getUniqueId());

        if (qPlayer == null) {
            main.getLogger().severe(Arrays.toString(new NullPointerException("Quest player not found").getStackTrace()));
            return;
        }

        QuestProgressFile qProgressFile = qPlayer.getQuestProgressFile();
        List<Quest> registeredQuests =
                q.getTaskTypeManager().getTaskType("fishcaught").getRegisteredQuests();
        for (Quest quest : registeredQuests) {
            // Check if player has started the quest
            if (qPlayer.hasStartedQuest(quest)) {
                // Iterate over each task of the quest
                for (Task task : quest.getTasksOfType("fishcaught")) {
                    // If incomplete, increment the progress
                    TaskProgress taskProgress = qProgressFile.getQuestProgress(quest).getTaskProgress(task.getId());
                    if (!taskProgress.isCompleted()) {
                        try {
                            int progressAmount = (int) taskProgress.getProgress();
                            int requiredAmount = (int) task.getConfigValue("amount");

                            if (!fishId.equalsIgnoreCase((String) task.getConfigValue("fishid"))) {
                                return;
                            }

                            taskProgress.setProgress(progressAmount + 1);

                            if (progressAmount + 1 >= requiredAmount) {
                                taskProgress.setCompleted(true);
                            }
                        } catch (NullPointerException ex) {
                            taskProgress.setProgress(1);
                        }
                    }
                }
            }
        }
    }
}
