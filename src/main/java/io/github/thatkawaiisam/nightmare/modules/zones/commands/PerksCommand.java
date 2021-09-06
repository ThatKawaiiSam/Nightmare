package io.github.thatkawaiisam.nightmare.modules.zones.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import io.github.thatkawaiisam.nightmare.modules.zones.ZoneModule;
import org.bukkit.command.CommandSender;

@CommandAlias("nightmare")
public class PerksCommand extends NightmareCommand {

    /**
     * Nightmare Perks Command.
     *
     * @param module instance.
     */
    public PerksCommand(ZoneModule module) {
        super(module);
    }

    @Subcommand("perks")
    public void onPerks(CommandSender sender) {
        sender.sendMessage(getModule().getPlugin().getLanguage().getValue("Command.Perks", true));
    }

}
