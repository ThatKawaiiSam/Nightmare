package io.github.thatkawaiisam.nightmare.modules.zones.events;

import io.github.thatkawaiisam.nightmare.modules.zones.Zone;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class ZoneEnterEvent extends Event {

    @Getter public static HandlerList handlerList = new HandlerList();

    private Zone zone;
    private Player player;

    /**
     * Capzone Enter Event.
     *
     * @param zone that was entered.
     * @param player that entered the capzone.
     */
    public ZoneEnterEvent(Zone zone, Player player) {
        this.zone = zone;
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
