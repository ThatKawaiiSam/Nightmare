package io.github.thatkawaiisam.nightmare.modules.zones.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import io.github.thatkawaiisam.nightmare.modules.zones.Zone;
import io.github.thatkawaiisam.nightmare.modules.zones.ZoneModule;
import io.github.thatkawaiisam.nightmare.modules.zones.ZoneState;
import io.github.thatkawaiisam.utils.MessageUtility;
import org.bukkit.command.CommandSender;

@CommandAlias("nightmare")
public class InfoCommand extends NightmareCommand {

    /**
     * Nightmare Information Command.
     *
     * @param module instance.
     */
    public InfoCommand(ZoneModule module) {
        super(module);
    }

    @Subcommand("info|information|view|list")
    public void onInformation(CommandSender sender) {
        sender.sendMessage(MessageUtility.formatMessage("&7&m-----------------------------------"));
        sender.sendMessage(MessageUtility.formatMessage("&5&lNightmare Event"));
        sender.sendMessage(MessageUtility.formatMessage(""));
        if (getModule().getZones().size() < 1) {
            sender.sendMessage(MessageUtility.formatMessage("&c&lThere are no Capzones currently setup!"));
            sender.sendMessage("");
        }
        for (Zone zone : getModule().getZones()) {
            sender.sendMessage(MessageUtility.formatMessage("&b" + zone.getDisplayName()));
            if (zone.getCenter() == null) {
                sender.sendMessage(MessageUtility.formatMessage("   &7Location: &cNot Set."));
            } else {
                sender.sendMessage(MessageUtility.formatMessage("   &7Location: &a" + zone.getCenter().getX() + ", " + zone.getCenter().getZ()));
            }
            if (zone.getCappingPlayer() != null) {
                if (zone.getState() == ZoneState.UNCONTROLLED) {
                    sender.sendMessage(MessageUtility.formatMessage("   &eCurrently being captured."));
                } else {
                    sender.sendMessage(MessageUtility.formatMessage("   &eCurrently being contested."));
                }
                if (!zone.getTimer().isPaused()) {
                    sender.sendMessage(MessageUtility.formatMessage("   &7Time Remaining: &f" + zone.getTimer().getTimeLeft()));
                }
            }
            if (zone.getCappedFaction() != null) {
                sender.sendMessage(MessageUtility.formatMessage("   &6Controlled by: &f" + zone.getCappedFaction().getTag()));
            }
            sender.sendMessage(" ");
        }
        sender.sendMessage(MessageUtility.formatMessage("&7&oTo view Nightmare Perks, use /nightmare perks"));
        sender.sendMessage(MessageUtility.formatMessage("&7&m-----------------------------------"));
    }

}
