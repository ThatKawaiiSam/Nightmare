package io.github.thatkawaiisam.nightmare.modules.zones.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import io.github.thatkawaiisam.nightmare.modules.zones.Zone;
import io.github.thatkawaiisam.nightmare.modules.zones.ZoneModule;
import org.bukkit.entity.Player;

@CommandAlias("nightmare")
public class TeleportCommand extends NightmareCommand {

    /**
     * Nightmare Teleport Command.
     *
     * @param module instance.
     */
    public TeleportCommand(ZoneModule module) {
        super(module);
    }

    @Subcommand("tp|teleport")
    @CommandPermission("Nightmare.Command.Teleport")
    @Syntax("<id>")
    public void onTeleport(Player player, String capId) {
        Zone zone = getModule().getCapzone(capId);
        // Check if a capzone exists.
        if (zone == null) {
            player.sendMessage(getModule().getPlugin().getLanguage().getValue("Command.No-Capzone", true));
            return;
        }

        // Check if a region is set.
        if (zone.getCenter() == null) {
            player.sendMessage(getModule().getPlugin().getLanguage().getValue("Command.No-Region-Set", true));
            return;
        }

        player.teleport(zone.getCenter());
        player.sendMessage(getModule().getPlugin().getLanguage().getValue("Command.Teleported", true).replace("{capzone}", zone.getDisplayName()));
    }

}
