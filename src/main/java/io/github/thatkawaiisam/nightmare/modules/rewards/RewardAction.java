package io.github.thatkawaiisam.nightmare.modules.rewards;

import com.massivecraft.factions.Faction;

public interface RewardAction {

    /**
     * When a faction gains their rewards from a tier.
     *
     * @param faction to apply to.
     */
    void onGain(Faction faction);

    /**
     * When a faction loses their rewards from a tier.
     *
     * @param faction to apply to.
     */
    void onLoss(Faction faction);

}
