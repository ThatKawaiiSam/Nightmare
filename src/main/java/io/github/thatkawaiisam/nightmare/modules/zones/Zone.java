package io.github.thatkawaiisam.nightmare.modules.zones;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import io.github.thatkawaiisam.filare.BukkitConfiguration;
import io.github.thatkawaiisam.utils.LocationUtility;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;

import java.util.UUID;

@Getter @Setter
public class Zone {

    private ZoneModule module;

    private Location minPosition, maxPosition;
    private String id, displayName;
    private ZoneState state = ZoneState.UNCONTROLLED;
    private Faction cappedFaction;
    private UUID cappingPlayer;
    private ZoneTimer timer;

    /**
     * Nightmare Zone.
     *
     * @param id of zone.
     */
    public Zone(String id, ZoneModule module) {
        this.id = id;
        this.displayName = id;
        this.module = module;
        this.timer = new ZoneTimer(this, module.getTimerManager());
        this.module.getTimerManager().addTimer(timer);
    }

    /**
     * Calculate lower location.
     *
     * @param loc1 value.
     * @param loc2 value.
     * @return correct value.
     */
    private Location getMinimum(Location loc1, Location loc2) {
        if (loc1 == null || loc2 == null) {
            return null;
        }
        return new Location(loc1.getWorld(), Math.min(loc1.getX(), loc2.getX()), Math.min(loc1.getY(), loc2.getY()), Math.min(loc1.getZ(), loc2.getZ()));
    }

    /**
     * Calculate higher location.
     *
     * @param loc1 value.
     * @param loc2 value.
     * @return correct value.
     */
    private Location getMaximum(Location loc1, Location loc2) {
        if (loc1 == null || loc2 == null) {
            return null;
        }
        return new Location(loc1.getWorld(), Math.max(loc1.getX(), loc2.getX()), Math.max(loc1.getY(), loc2.getY()), Math.max(loc1.getZ(), loc2.getZ()));
    }

    /**
     * Calculations for min and max inside.
     *
     * @param pos location.
     * @param pos2 location.
     * @param pos3 location.
     * @return if position is placed correctly.
     */
    private boolean isInAABB(Location pos, Location pos2, Location pos3) {
        Location min = getMinimum(pos2, pos3);
        Location max = getMaximum(pos2, pos3);
        if (min == null || max == null) {
            return false;
        }
        // Check all of the vectors.
        if (min.getBlockX() <= pos.getBlockX() &&
                max.getBlockX() >= pos.getBlockX() &&
                min.getBlockY() <= pos.getBlockY() &&
                max.getBlockY() >= pos.getBlockY() &&
                min.getBlockZ() <= pos.getBlockZ() &&
                max.getBlockZ() >= pos.getBlockZ()) {
            return true;
        }
        return false;
    }

    /**
     * Check whether the location is inside the bounds of the zone.
     *
     * @param location of player.
     * @return whether the player is inside or not.
     */
    public boolean isInside(Location location) {
        if (this.minPosition == null
                || this.maxPosition == null
                || location == null) {
            return false;
        }
        if (location.getWorld() == this.minPosition.getWorld()) {
            if (isInAABB(location, minPosition, maxPosition)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates the center location based of the minimum and maximum locations.
     *
     * @return location.
     */
    public Location getCenter() {
        if (getMinPosition() == null || getMaxPosition() == null) {
            return null;
        }
        int x = (maxPosition.getBlockX() + minPosition.getBlockX()) / 2;
        int y = (maxPosition.getBlockY() + minPosition.getBlockY()) / 2;
        int z = (maxPosition.getBlockZ() + minPosition.getBlockZ()) / 2;

        return new Location(minPosition.getWorld(), x, y, z);
    }

    /**
     * Load Capzone data from file.
     *
     * @param config file to load from.
     */
    public void load(Configuration config) {
        // Load Locations.
        String firstLocationString = config.getString("Zones." + getId() + ".First-Location");
        String secondLocationString = config.getString("Zones." + getId() + ".Second-Location");
        if (firstLocationString != null && secondLocationString != null) {
            Location firstLocation = LocationUtility.parseToLocation(firstLocationString);
            Location secondLocation = LocationUtility.parseToLocation(secondLocationString);
            this.minPosition = getMinimum(firstLocation, secondLocation);
            this.maxPosition = getMaximum(firstLocation, secondLocation);
        }
        if (config.contains("Zones." + getId() + ".Display-Name")) {
            this.displayName = config.getString("Zones." + getId() + ".Display-Name");
        }
        // Attempt to reapply capped faction if available.
        if (config.contains("Zones." + getId() + ".Capped-Faction")) {
            String cappedFactionID = config.getString("Zones." + getId() + ".Capped-Faction");
            Faction faction = Factions.getInstance().getFactionById(cappedFactionID);
            if (faction != null) {
                this.cappedFaction = faction;
                this.state = ZoneState.IN_CONTROL;
            }
        }
    }

    /**
     * Delete Zone data to file.
     *
     * @param config file to modify.
     */
    public void delete(BukkitConfiguration config) {
        config.getImplementation().set("Zones." + getId(), null);
        config.save();
    }

    /**
     * Save Zone data to file.
     */
    public void save(BukkitConfiguration c) {
        Configuration config = c.getImplementation();
        // Save Locations.
        if (this.minPosition != null && this.maxPosition != null) {
            String firstLocation = LocationUtility.parseToString(this.minPosition);
            String secondLocation = LocationUtility.parseToString(this.maxPosition);
            config.set("Zones." + getId() + ".First-Location", firstLocation);
            config.set("Zones." + getId() + ".Second-Location", secondLocation);
        }
        // Persist capped faction if available.
        if (cappedFaction != null) {
            config.set("Zones." + getId() + ".Capped-Faction", cappedFaction.getId());
        } else {
            config.set("Zones." + getId() + ".Capped-Faction", null);
        }
        if (displayName != null) {
            config.set("Zones." + getId() + ".Display-Name", displayName);
        }
        // TODO: See if we can move this is the main method to avoid long rewrites.
        c.save();
    }

}
