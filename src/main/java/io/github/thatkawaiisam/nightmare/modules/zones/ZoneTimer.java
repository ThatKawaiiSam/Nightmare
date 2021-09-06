package io.github.thatkawaiisam.nightmare.modules.zones;

import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import io.github.thatkawaiisam.nightmare.modules.rewards.RewardsModule;
import io.github.thatkawaiisam.timers.TimerManager;
import io.github.thatkawaiisam.timers.api.Timer;
import io.github.thatkawaiisam.timers.api.TimerType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class ZoneTimer extends Timer {

    private Zone zone;

    /**
     * Capzone Timer.
     *
     * @param zone the timer belongs to.
     * @param manager instance.
     */
    public ZoneTimer(Zone zone, TimerManager manager) {
        super(TimerType.COUNTDOWN, manager);
        this.zone = zone;
        this.setRemoveOnCompletion(false);
        this.setPaused(true);
        this.setDuration(TimeUnit.MINUTES.toMillis(5));
    }

    @Override
    public void tick() {
        // Final Minute.
        if (getMinutes(getRemaining()) == 0) {
            if (
                getSeconds(getRemaining()) == 30 ||
                getSeconds(getRemaining()) == 10 ||
                getSeconds(getRemaining()) == 3 ||
                getSeconds(getRemaining()) == 2 ||
                getSeconds(getRemaining()) == 1
            ) {
                sendTimeLeft(false);
                return;
            }
        }
        // Every Minute!
        if (getSeconds(getRemaining()) % 60 == 0) {
            if (getMinutes(getRemaining()) == 0) {
                return;
            }
            sendTimeLeft(false);
        }
    }

    private void sendTimeLeft(boolean start) {
        if (zone.getState() == ZoneState.UNCONTROLLED) {
            if (start) {
                this.setEndTime(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10));
            }
            Bukkit.broadcastMessage(
                    zone.getModule().getPlugin()
                            .getLanguage()
                            .getValue("Chat.Capzone-Current-Captured", true)
                            .replace("{capzone}", zone.getDisplayName())
                            .replace("{time}", getTimeLeft())
            );
        } else {
            if (start) {
                this.setEndTime(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5));
            }
            Bukkit.broadcastMessage(
                    zone.getModule().getPlugin()
                            .getLanguage()
                            .getValue("Chat.Capzone-Current-Contested", true)
                            .replace("{capzone}", zone.getDisplayName())
                            .replace("{time}", getTimeLeft())
            );
        }
    }

    @Override
    public void onStart() {
        this.setPaused(false);
        sendTimeLeft(true);
    }

    @Override
    public void onComplete() {
        new BukkitRunnable(){
            @Override
            public void run() {
                setPaused(true);
                if (zone.getState() == ZoneState.UNCONTROLLED) {
                    zone.setState(ZoneState.IN_CONTROL);
                    if (zone.getCappingPlayer() != null) {
                        Faction faction = FPlayers.getInstance().getByPlayer(Bukkit.getPlayer(zone.getCappingPlayer())).getFaction();
                        zone.setCappedFaction(faction);
                        zone.setCappingPlayer(null);
                        Bukkit.broadcastMessage(
                            zone.getModule().getPlugin()
                                .getLanguage()
                                .getValue("Chat.Capzone-Capture-Success", true)
                                .replace("{capzone}", zone.getDisplayName())
                                .replace("{faction}", faction.getTag())
                        );

                        int rewardLevel = zone.getModule().rewardLevel(zone.getCappedFaction());
                        getRewardsModule().getReward(rewardLevel).onGain(zone.getCappedFaction());
                        for (Player factionPlayer : faction.getOnlinePlayers()) {
                            factionPlayer.sendMessage(
                                zone.getModule().getPlugin()
                                    .getLanguage()
                                    .getValue("Chat.Faction-Reward-Upgrade", true)
                                    .replace("{level}", rewardLevel + "")
                            );
                        }
                    } else {
                        // The Capzone just got capped by an invisible person :)
                    }
                    setPaused(true);
                } else {
                    zone.setState(ZoneState.UNCONTROLLED);
                    final Faction previouslyCappingFaction = zone.getCappedFaction();
                    Bukkit.broadcastMessage(
                            zone.getModule().getPlugin()
                                    .getLanguage()
                                    .getValue("Chat.Capzone-Contested-Success", true)
                                    .replace("{capzone}", zone.getDisplayName())
                                    .replace("{faction}", FPlayers.getInstance().getByPlayer(Bukkit.getPlayer(zone.getCappingPlayer())).getFaction().getTag())
                    );
                    zone.setCappedFaction(null);
                    if (previouslyCappingFaction != null) {
                        int rewardLevel = zone.getModule().rewardLevel(previouslyCappingFaction);
                        if (rewardLevel > 0) {
                            getRewardsModule().getReward(rewardLevel + 1).onLoss(previouslyCappingFaction);
                            getRewardsModule().getReward(rewardLevel).onGain(previouslyCappingFaction);
                        } else {
                            getRewardsModule().getReward(rewardLevel + 1).onLoss(previouslyCappingFaction);
                        }
                        for (Player player : previouslyCappingFaction.getOnlinePlayers()) {
                            player.sendMessage(
                                zone.getModule().getPlugin()
                                    .getLanguage()
                                    .getValue("Faction-Reward-Loss", true)
                                    .replace("{level}", (rewardLevel+1) + "")
                            );
                        }
                    }
                    setPaused(false);
                    start();
                    setPaused(false);
                }
            }
        }.runTask(zone.getModule().getPlugin());
    }

    @Override
    public void onCancel() {

    }

    /**
     * Gets the Nightmare Rewards Handler.
     *
     * @return handler.
     */
    private RewardsModule getRewardsModule() {
        return zone.getModule().getPlugin().getModuleFactory().getModule("rewards");
    }

    /**
     * Gets the time in a more presentable format.
     *
     * @return formatted time.
     */
    public String getTimeLeft() {
        long millis = getRemaining();
        if (isPaused()) {
            millis = getDuration();
        }
        final int sec = (int) (millis / 1000 % 60);
        final int min = (int) (millis / 60000 % 60);
        final int hr = (int) (millis / 3600000 % 24);
        return ((hr > 0) ? String.format("%02d:", hr) : "") + String.format("%02d:%02d", min, sec);
    }

}
