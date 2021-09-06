package io.github.thatkawaiisam.nightmare.modules.rewards;

import io.github.thatkawaiisam.artus.bukkit.BukkitModule;
import io.github.thatkawaiisam.nightmare.NightmarePlugin;
import org.bukkit.configuration.Configuration;

import java.util.HashSet;
import java.util.Set;

public class RewardsModule extends BukkitModule<NightmarePlugin> {

    // Reward Tiers.
    private Set<NightmareReward> rewards = new HashSet<>();

    /**
     * Rewards Module.
     *
     * @param plugin instance.
     */
    public RewardsModule(NightmarePlugin plugin) {
        super(plugin, "rewards");
        getOptions().setGenerateConfiguration(true);
    }

    @Override
    public void onEnable() {
        Configuration c = getConfiguration().getImplementation();
        for (String key : c.getConfigurationSection("Tiers").getKeys(false)) {
            int rewardTier = Integer.parseInt(key);

            // Gain.
            String gainCommand = c.getString("Tiers." + rewardTier + ".Gain");
            if (gainCommand == null || gainCommand.isEmpty()) {
                getPlugin().getLogger().warning("Reward tier '" + rewardTier + "' does not have any gain effects.");
            }

            // Loss.
            String lossCommand = c.getString("Tiers." + rewardTier + ".Loss");
            if (lossCommand == null || lossCommand.isEmpty()) {
                getPlugin().getLogger().warning("Reward tier '" + rewardTier + "' does not have any loss effects.");
            }

            rewards.add(new NightmareReward(rewardTier, gainCommand, lossCommand));
            getPlugin().getLogger().info("Loaded '" + rewardTier + "' reward tier.");
        }
    }

    @Override
    public void onDisable() {
        rewards.clear();
    }

    /**
     * Get Nightmare Reward based on it's tier.
     *
     * @param tier of nightmare reward.
     * @return reward if it exists.
     */
    public NightmareReward getReward(int tier) {
        for (NightmareReward reward : rewards) {
            if (reward.getTier() == tier) {
                return reward;
            }
        }
        return null;
    }

}
