package io.github.thatkawaiisam.nightmare.modules.zones.events;

import io.github.thatkawaiisam.nightmare.modules.zones.Zone;
import io.github.thatkawaiisam.nightmare.modules.zones.ZoneState;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter @Setter
public class ZoneStateChangeEvent extends Event {

    @Getter public static HandlerList handlerList = new HandlerList();

    private Zone zone;
    private ZoneState previousState, newState;

    /**
     * Capzone State Change Event.
     *
     * @param zone that's state has changed.
     * @param previousState of capzone.
     * @param newState of capzone.
     */
    public ZoneStateChangeEvent(Zone zone, ZoneState previousState, ZoneState newState) {
        this.zone = zone;
        this.previousState = previousState;
        this.newState = newState;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
