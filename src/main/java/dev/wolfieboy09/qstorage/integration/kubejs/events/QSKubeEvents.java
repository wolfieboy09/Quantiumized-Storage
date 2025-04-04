package dev.wolfieboy09.qstorage.integration.kubejs.events;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface QSKubeEvents {
    EventGroup GROUP = EventGroup.of("QSEvents");

    EventHandler DATA_MAP_EVENT = GROUP.server("registerFuels", () -> QSDataMapEvent.class);
}
