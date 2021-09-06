package io.github.thatkawaiisam.nightmare;

import io.github.thatkawaiisam.artus.bukkit.BukkitPlugin;
import io.github.thatkawaiisam.artus.bukkit.language.BukkitLanguageModule;
import io.github.thatkawaiisam.nightmare.modules.rewards.RewardsModule;
import io.github.thatkawaiisam.nightmare.modules.zones.ZoneModule;

public class NightmarePlugin extends BukkitPlugin {

    @Override
    public void onEnable() {
        // Add modules.
        this.getModuleFactory().addModule(new BukkitLanguageModule<>(this));
        this.getModuleFactory().addModule(new RewardsModule(this));
        this.getModuleFactory().addModule(new ZoneModule(this));

        // Enable modules.
        this.getModuleFactory().enableModules();
    }

    @Override
    public void onDisable() {
        // Disable modules.
        this.getModuleFactory().disableModules();
    }

    /**
     * Get Language Module.
     *
     * @return module instance.
     */
    public BukkitLanguageModule<NightmarePlugin> getLanguage() {
        return this.getModuleFactory().getModule("lang");
    }

}
