package io.github.thatkawaiisam.nightmare.modules.zones.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.HelpCommand;
import io.github.thatkawaiisam.artus.bukkit.BukkitCommand;
import io.github.thatkawaiisam.nightmare.modules.zones.ZoneModule;
import io.github.thatkawaiisam.utils.MessageUtility;
import org.bukkit.command.CommandSender;

@CommandAlias("nightmare")
public class NightmareCommand extends BukkitCommand<ZoneModule> {

    /**
     * Base Nightmare Command.
     *
     * @param module instance.
     */
    public NightmareCommand(ZoneModule module) {
        super(module);
    }

    @Default
    @HelpCommand
    public void onDefault(CommandSender sender) {
        sender.sendMessage(MessageUtility.formatMessage("&7&m-----------------------------------"));
        sender.sendMessage(MessageUtility.formatMessage("&5&lNightmare Event"));
        sender.sendMessage(MessageUtility.formatMessage(""));
        sender.sendMessage(MessageUtility.formatMessage("&e&o/nightmare info &7&oto view information."));
        sender.sendMessage(MessageUtility.formatMessage("&e&o/nightmare admin &7&oto view admin commands."));
        sender.sendMessage(MessageUtility.formatMessage("&7&m-----------------------------------"));
    }

}
