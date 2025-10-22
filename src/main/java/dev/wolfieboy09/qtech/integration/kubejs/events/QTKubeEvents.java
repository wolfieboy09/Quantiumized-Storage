package dev.wolfieboy09.qtech.integration.kubejs.events;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.wolfieboy09.qtech.integration.kubejs.events.datamap.QTSmelteryDataMapEvent;

public interface QTKubeEvents {
    EventGroup GROUP = EventGroup.of("QTEvents");

    EventHandler DATA_MAP_EVENT = GROUP.server("registerFuels", () -> QTSmelteryDataMapEvent.class);
}
