package io.github.thatkawaiisam.nightmare.modules.zones.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import com.boydti.fawe.object.FawePlayer;
import com.sk89q.worldedit.regions.Region;
import io.github.thatkawaiisam.nightmare.modules.zones.Zone;
import io.github.thatkawaiisam.nightmare.modules.zones.ZoneModule;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@CommandAlias("nightmare")
public class SetRegionCommand extends NightmareCommand {

    /**
     * Nightmare Set Region Command.
     *
     * @param module instance.
     */
    public SetRegionCommand(ZoneModule module) {
        super(module);
    }

    @Subcommand("setregion|setselection")
    @CommandPermission("Nightmare.Command.SetRegion")
    @Syntax("<id>")
    public void onRegion(Player player, String capId) {
        Zone zone = getModule().getCapzone(capId);

        // Make sure capzone exists.
        if (zone == null) {
            player.sendMessage(getModule().getPlugin().getLanguage().getValue("Command.No-Capzone", true));
            return;
        }
        FawePlayer fawePlayer = FawePlayer.wrap(player);
        if (fawePlayer == null) {
            player.sendMessage(getModule().getPlugin().getLanguage().getValue("Command.Selection-Error", true));
            return;
        }

        // Get Region Selection from World Edit.
        Region region = fawePlayer.getSelection();
        if (region == null) {
            player.sendMessage(getModule().getPlugin().getLanguage().getValue("Command.Selection-Error", true));
            return;
        }

        // Get points, set the capzone, and save to configuration section.
        Location minimumPoint = new Location(player.getWorld(), region.getMinimumPoint().getX(), region.getMinimumPoint().getY(), region.getMinimumPoint().getZ());
        Location maximumPoint = new Location(player.getWorld(), region.getMaximumPoint().getX(), region.getMaximumPoint().getY(), region.getMaximumPoint().getZ());
        zone.setMinPosition(minimumPoint);
        zone.setMaxPosition(maximumPoint);
        zone.save(getModule().getConfiguration());

        // Send success message.
        player.sendMessage(getModule().getPlugin().getLanguage().getValue("Command.Selection-Success", true));
    }

}
