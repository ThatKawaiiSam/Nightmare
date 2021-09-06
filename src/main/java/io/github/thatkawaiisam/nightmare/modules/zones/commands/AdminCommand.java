package io.github.thatkawaiisam.nightmare.modules.zones.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import io.github.thatkawaiisam.nightmare.NightmarePlugin;
import io.github.thatkawaiisam.nightmare.modules.zones.ZoneModule;
import io.github.thatkawaiisam.utils.MessageUtility;
import org.bukkit.command.CommandSender;

@CommandAlias("nightmare")
public class AdminCommand extends NightmareCommand {

    /**
     * Nightmare Admin Command.
     *
     * @param module instance.
     */
    public AdminCommand(ZoneModule module) {
        super(module);
    }

    @Subcommand("admin|admincmds|adminhelp|admin help|admin ?")
    @CommandPermission("Nightmare.Command.Admin")
    public void onAdmin(CommandSender sender) {
        sender.sendMessage(MessageUtility.formatMessage("&7&m-----------------------------------"));
        sender.sendMessage(MessageUtility.formatMessage("&5&lNightmare Event Admin Commands"));
        sender.sendMessage(MessageUtility.formatMessage(""));
        sender.sendMessage(MessageUtility.formatMessage("/nightmare create <id> - Creates a capzone."));
        sender.sendMessage(MessageUtility.formatMessage("/nightmare delete <id> - Deletes a capzone."));
        sender.sendMessage(MessageUtility.formatMessage("/nightmare setregion <id> - Sets the region of a capzone."));
        sender.sendMessage(MessageUtility.formatMessage("/nightmare teleport <id> - Teleports you to the capzone."));
        sender.sendMessage(MessageUtility.formatMessage("/nightmare reset - Resets the current factions on capzones and their rewards."));
        sender.sendMessage(MessageUtility.formatMessage("&7&m-----------------------------------"));
    }

}
