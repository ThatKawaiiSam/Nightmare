package io.github.thatkawaiisam.nightmare.modules.zones.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import io.github.thatkawaiisam.nightmare.modules.zones.Zone;
import io.github.thatkawaiisam.nightmare.modules.zones.ZoneModule;
import org.bukkit.command.CommandSender;

@CommandAlias("nightmare")
public class DeleteCommand extends NightmareCommand {

    /**
     * Nightmare Delete Command.
     *
     * @param module instance.
     */
    public DeleteCommand(ZoneModule module) {
        super(module);
    }

    @Subcommand("delete|del")
    @CommandPermission("Nightmare.Command.Delete")
    @Syntax("<id>")
    public void onDelete(CommandSender sender, String id) {
        Zone zone = getModule().getCapzone(id);
        // Check if a Capzone already exists with that name.
        if (zone == null) {
            sender.sendMessage(getModule().getPlugin().getLanguage().getValue("Command.No-Capzone", true));
            return;
        }

        // Create Capzone Object and save to configuration.
        zone.delete(getModule().getConfiguration());
        getModule().getZones().remove(zone);

        // Send deletion message.
        sender.sendMessage(
                getModule().getPlugin().getLanguage().getValue("Command.Capzone-Delete", true)
        );
    }

}
