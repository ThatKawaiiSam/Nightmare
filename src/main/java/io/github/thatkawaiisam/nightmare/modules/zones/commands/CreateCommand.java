package io.github.thatkawaiisam.nightmare.modules.zones.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import io.github.thatkawaiisam.nightmare.modules.zones.Zone;

import io.github.thatkawaiisam.nightmare.modules.zones.ZoneModule;
import org.bukkit.command.CommandSender;

@CommandAlias("nightmare")
public class CreateCommand extends NightmareCommand {

    /**
     * Nightmare Create Command.
     *
     * @param module instance.
     */
    public CreateCommand(ZoneModule module) {
        super(module);
    }

    @Subcommand("create")
    @CommandPermission("Nightmare.Command.Create")
    @Syntax("<id>")
    public void onCreate(CommandSender sender, String id) {
        // Check if a capzone already exists with that name.
        if (getModule().getCapzone(id) != null) {
            sender.sendMessage(getModule().getPlugin().getLanguage().getValue("Command.Already-Exists", true));
            return;
        }

        // Create Capzone Object and save to configuration.
        Zone zone = new Zone(id, getModule());
        getModule().getZones().add(zone);
        zone.save(getModule().getConfiguration());

        // Send success message.
        sender.sendMessage(
                getModule().getPlugin().getLanguage()
                    .getValue("Command.Capzone-Create", true)
                        .replace("{capzone}", id)
        );
    }

}
