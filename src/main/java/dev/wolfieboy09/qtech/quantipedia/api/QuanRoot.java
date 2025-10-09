package dev.wolfieboy09.qtech.quantipedia.api;

import dev.wolfieboy09.qtech.quantipedia.api.errors.QuanEntryAlreadyExists;

import java.util.*;

public record QuanRoot(String modId, Set<QuanEntry> entries) {
    public QuanRoot(String modId) {
        this(modId, new TreeSet<>(
                Comparator.comparing(QuanEntry::header, String.CASE_INSENSITIVE_ORDER)
        ));
    }

    public void addEntry(QuanEntry entry) {
        if (!this.entries.add(entry)) {
            throw new QuanEntryAlreadyExists("The entry already exists: " + entry);
        }
    }
}
