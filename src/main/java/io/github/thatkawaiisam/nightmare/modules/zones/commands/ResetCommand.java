package io.github.thatkawaiisam.nightmare.modules.zones.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import com.massivecraft.factions.Faction;
import io.github.thatkawaiisam.nightmare.modules.rewards.RewardsModule;
import io.github.thatkawaiisam.nightmare.modules.zones.Zone;
import io.github.thatkawaiisam.nightmare.modules.zones.ZoneModule;
import io.github.thatkawaiisam.nightmare.modules.zones.ZoneState;
import io.github.thatkawaiisam.utils.MessageUtility;
import org.bukkit.command.CommandSender;

@CommandAlias("nightmare")
public class ResetCommand extends NightmareCommand {

    /**
     * Nightmare Reset Cappers Command.
     *
     * @param module instance.
     */
    public ResetCommand(ZoneModule module) {
        super(module);
    }

    @Subcommand("reset")
    @CommandPermission("Nightmare.Command.Reset")
    public void onReset(CommandSender sender) {
        for (Zone zone : getModule().getZones()) {
            Faction cappedFaction = zone.getCappedFaction();
            zone.setState(ZoneState.UNCONTROLLED);
            zone.setCappedFaction(null);
            zone.getTimer().setPaused(true);
            int rewardLevel = getModule().rewardLevel(cappedFaction);
            if (rewardLevel < 1) {
                continue;
            }
            getModule().getPlugin().getModuleFactory().<RewardsModule>getModule("rewards").getReward(rewardLevel).onLoss(cappedFaction);
        }
        sender.sendMessage(MessageUtility.formatMessage("&aSuccessfully reset cappers and their rewards."));
    }

}
