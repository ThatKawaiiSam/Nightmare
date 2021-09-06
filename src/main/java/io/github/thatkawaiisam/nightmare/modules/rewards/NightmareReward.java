package io.github.thatkawaiisam.nightmare.modules.rewards;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

@Getter @Setter
public class NightmareReward implements RewardAction {

    private int tier;
    private String gainCommand;
    private String lossCommand;

    /**
     * Nightmare Reward.
     *
     * @param tier of the reward.
     * @param gainCommand to be executed.
     * @param lossCommand to be executed.
     */
    public NightmareReward(int tier, String gainCommand, String lossCommand) {
        this.tier = tier;
        this.gainCommand = gainCommand;
        this.lossCommand = lossCommand;
    }

    @Override
    public void onGain(Faction faction) {
        if (gainCommand == null || gainCommand.isEmpty()) {
            return;
        }
        executeCommand(faction, gainCommand);
    }

    @Override
    public void onLoss(Faction faction) {
        if (lossCommand == null || lossCommand.isEmpty()) {
            return;
        }
        executeCommand(faction, lossCommand);
    }

    /**
     * Executes a command to the faction.
     *
     * @param faction to execute command on.
     * @param command to execute.
     */
    private void executeCommand(Faction faction, String command) {
        for (FPlayer fPlayer : faction.getFPlayers()) {
            String finalCommand = command.replace("{factionPlayer}", fPlayer.getPlayer().getName());
            Bukkit.dispatchCommand(
                    Bukkit.getConsoleSender(),
                    finalCommand
            );
        }
    }
}
