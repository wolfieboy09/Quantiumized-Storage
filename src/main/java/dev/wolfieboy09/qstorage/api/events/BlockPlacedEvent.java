package dev.wolfieboy09.qstorage.api.events;

import net.neoforged.bus.api.Event;

public class BlockPlacedEvent extends Event {
    public void doTheThing() {
        System.out.println("I have been called");
    }
}
