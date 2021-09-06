package io.github.thatkawaiisam.nightmare.modules.zones;

import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import io.github.thatkawaiisam.artus.bukkit.BukkitListener;
import io.github.thatkawaiisam.nightmare.modules.zones.events.ZoneEnterEvent;
import io.github.thatkawaiisam.nightmare.modules.zones.events.ZoneLeaveEvent;
import io.github.thatkawaiisam.nightmare.modules.zones.events.ZoneStateChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class ZoneListeners extends BukkitListener<ZoneModule> {

    /**
     * Zone Listeners.
     *
     * @param module instance.
     */
    public ZoneListeners(ZoneModule module) {
        super(module);
    }

    @EventHandler
    public void onCapzoneEnter(ZoneEnterEvent event) {
        // Entered Message.
        event.getPlayer().sendMessage(
                this.getModule().getPlugin().getLanguage()
                        .getValue("Command.Entered-Region", true)
                        .replace("{capzone}", event.getZone().getDisplayName())
        );

        // Check if player is in the correct faction.
        Faction playerFaction = FPlayers.getInstance().getByPlayer(event.getPlayer()).getFaction();
        if (!getModule().isApplicableFaction(playerFaction)) {
            return;
        }

        // Check.
        if (event.getZone().getCappingPlayer() == null) {
            if (event.getZone().getCappedFaction() != null && event.getZone().getCappedFaction() == playerFaction) {
                // Your faction already has control over this land.
                return;
            }
            event.getZone().setCappingPlayer(event.getPlayer().getUniqueId());
            event.getZone().getTimer().setPaused(false);
            event.getZone().getTimer().start();
        }
    }

    @EventHandler
    public void onCapzoneLeave(ZoneLeaveEvent event) {
        // Left Message.
        event.getPlayer().sendMessage(
                getModule().getPlugin().getLanguage()
                        .getValue("Command.Left-Region", true)
                        .replace("{capzone}", event.getZone().getDisplayName())
        );

        // Check if player is in the correct faction.
        Faction playerFaction = FPlayers.getInstance().getByPlayer(event.getPlayer()).getFaction();
        if (!getModule().isApplicableFaction(playerFaction)) {
            return;
        }

        // TODO: Find new people.
        if (event.getZone().getCappingPlayer() != null
                && event.getZone().getCappingPlayer() == event.getPlayer().getUniqueId()) {
            event.getZone().setCappingPlayer(null);
            event.getZone().getTimer().setPaused(true);
            if (event.getZone().getState() == ZoneState.UNCONTROLLED) {
                Bukkit.broadcastMessage(
                        getModule().getPlugin().getLanguage()
                                .getValue("Chat.Capzone-No-Capping", true)
                                .replace("{capzone}", event.getZone().getDisplayName())
                );
            } else {
                Bukkit.broadcastMessage(
                        getModule().getPlugin().getLanguage()
                                .getValue("Chat.Capzone-No-Contesting", true)
                                .replace("{capzone}", event.getZone().getDisplayName())
                );
            }

            // Allocate Loop player.
            for (Player loopPlayer : Bukkit.getOnlinePlayers()) {
                if (loopPlayer == event.getPlayer()) {
                    continue;
                }
                // Check if they are in the zone.
                if (!event.getZone().isInside(loopPlayer.getLocation())) {
                    continue;
                }
                // Check if player is in the correct faction.
                Faction loopPlayerFaction = FPlayers.getInstance().getByPlayer(loopPlayer).getFaction();
                if (!getModule().isApplicableFaction(loopPlayerFaction)) {
                    continue;
                }
                if (event.getZone().getCappedFaction() != null && event.getZone().getCappedFaction() == loopPlayerFaction) {
                    // Your faction already has control over this land.
                    return;
                }
                // Allocate.
                event.getZone().setCappingPlayer(loopPlayer.getUniqueId());
                event.getZone().getTimer().setPaused(false);
                event.getZone().getTimer().start();
                return;
            }
        }
    }

    @EventHandler
    public void onCap(ZoneStateChangeEvent event) {
        if (event.getNewState() == ZoneState.IN_CONTROL) {
//            Bukkit.broadcastMessage("CAP is now controlled by ThatKawaiiSam.");
            // Broadcast Rewards

        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.handleLocationChange(event.getPlayer(), null, event.getPlayer().getLocation());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        this.handleLocationChange(event.getPlayer(), event.getPlayer().getLocation(), null);
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        this.handleLocationChange(event.getPlayer(), event.getTo(), event.getFrom());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        this.handleLocationChange(event.getPlayer(), event.getTo(), event.getFrom());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        this.handleLocationChange(event.getEntity(), null, event.getEntity().getLocation());
    }

    /**
     * Handles the location change for several different events.
     *
     * @param player to check.
     * @param to location.
     * @param from location.
     */
    private void handleLocationChange(Player player, Location to, Location from) {
        // Loop through each of the capzones, and call the according events.
        for (Zone zone : getModule().getZones()) {
            final boolean wasIn = zone.isInside(from);
            final boolean isIn = zone.isInside(to);

            // TODO: Add the ability to make these events cancellable!

            // Has entered Capzone.
            if (!wasIn && isIn) {
                Bukkit.getPluginManager().callEvent(new ZoneEnterEvent(zone, player));
            }

            // Has left Capzone.
            else if (wasIn && !isIn) {
                Bukkit.getPluginManager().callEvent(new ZoneLeaveEvent(zone, player));
            }
        }
    }

}
