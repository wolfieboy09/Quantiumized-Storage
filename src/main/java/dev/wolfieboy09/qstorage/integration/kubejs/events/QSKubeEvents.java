package dev.wolfieboy09.qstorage.integration.kubejs.events;

import dev.latvian.mods.kubejs.event.EventGroup;

public interface QSKubeEvents {
    EventGroup GROUP = EventGroup.of("QSEvents");

    //EventHandler REGISTER_GAS = QSKubeEvents.GROUP.startup("registerGas", () -> QSGasJS.class);

}
