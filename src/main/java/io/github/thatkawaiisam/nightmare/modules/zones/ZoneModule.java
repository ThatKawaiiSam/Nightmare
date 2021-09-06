package io.github.thatkawaiisam.nightmare.modules.zones;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import io.github.thatkawaiisam.artus.bukkit.BukkitModule;
import io.github.thatkawaiisam.nightmare.NightmarePlugin;
import io.github.thatkawaiisam.nightmare.modules.rewards.RewardsModule;
import io.github.thatkawaiisam.nightmare.modules.zones.commands.*;
import io.github.thatkawaiisam.timers.TimerManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ZoneModule extends BukkitModule<NightmarePlugin> {

    @Getter private TimerManager timerManager;
    @Getter private List<Zone> zones = new ArrayList<>();

    /**
     * Zone Module.
     *
     * @param plugin instance.
     */
    public ZoneModule(NightmarePlugin plugin) {
        super(plugin, "zone");
        getOptions().setGenerateConfiguration(true);
    }

    @Override
    public void onEnable() {
        timerManager = new TimerManager();
        Configuration c = getConfiguration().getImplementation();
        if (!c.contains("Zones") || c.getConfigurationSection("Zones") == null) {
            Bukkit.getLogger().warning("[CAPZONE] Be sure to set up some capzones!");
        } else {
            // Loading all of the zones from the configuration.
            for (String key : c.getConfigurationSection("Zones").getKeys(false)) {
                Zone zone = new Zone(key, this);
                zone.load(getConfiguration().getImplementation());
                zones.add(zone);
                Bukkit.getLogger().info("[CAPZONE] " + zone.getId() + " has been loaded!");
            }
            // Attempts to recalculate all of the factions reward tier.
            for (Faction faction : Factions.getInstance().getAllFactions()) {
                Bukkit.getLogger().info("Attempting to recalculate " + faction.getTag() + " faction.");
                rewardsRecalculate(faction);
            }
        }

        // Register Listeners and Commands
        this.addListener(new ZoneListeners(this));
        this.addCommand(new AdminCommand(this));
        this.addCommand(new CreateCommand(this));
        this.addCommand(new DeleteCommand(this));
        this.addCommand(new InfoCommand(this));
        this.addCommand(new PerksCommand(this));
        this.addCommand(new ResetCommand(this));
        this.addCommand(new SetRegionCommand(this));
        this.addCommand(new TeleportCommand(this));
    }

    @Override
    public void onDisable() {
        for (Zone zone : this.zones) {
            zone.save(getConfiguration());
            Bukkit.getLogger().info("[CAPZONE] " + zone.getId() + " has been saved!");
        }
        zones.clear();
        timerManager.cleanup();
    }

    /**
     * Recalculates the rewards application on reload or server start.
     *
     * @param faction that is being calculated.
     */
    public void rewardsRecalculate(Faction faction) {
        int tierAmount = rewardLevel(faction);
        if (tierAmount < 1) {
            // They actually aren't controlling any capzones.
            return;
        }
        // Give them the gained effects.
        getPlugin().getModuleFactory().<RewardsModule>getModule("rewards").getReward(tierAmount).onGain(faction);
    }

    /**
     * Calculate the reward level of a faction based on the amount of capzones in control.
     *
     * @param faction to check.
     * @return tier level. Note: 0 indicates that they aren't in control of any zones.
     */
    public int rewardLevel(Faction faction) {
        int tierAmount = 0;
        for (Zone zone : this.zones) {
            if (zone.getCappedFaction() == faction) {
                tierAmount++;
            }
        }
        return tierAmount;
    }

    /**
     * Gets a Capzone from the Local cache from it's ID.
     *
     * @param id of capzone you wish to access.
     * @return capzone if it exists.
     */
    public Zone getCapzone(String id) {
        for (Zone zone : this.zones) {
            if (zone.getId().equalsIgnoreCase(id)) {
                return zone;
            }
        }
        return null;
    }

    /**
     * Check if Faction is applicable for Nightmare event.
     *
     * @param faction to check.
     * @return if it is applicable.
     */
    public boolean isApplicableFaction(Faction faction) {
        return faction != null
                && !faction.isWarZone()
                && !faction.isWilderness()
                && !faction.isSafeZone()
                && !faction.isPlayerFreeType();
    }

    /**
     * Gets the Capzone of Player.
     *
     * @param player to check.
     * @return capzone of player if they are in one.
     */
    public Zone isPlayerInCapzone(Player player) {
        for (Zone zone : this.zones) {
            if (zone.isInside(player.getLocation())) {
                return zone;
            }
        }
        return null;
    }

}
