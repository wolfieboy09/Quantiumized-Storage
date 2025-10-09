package dev.wolfieboy09.qtech.quantipedia.api.errors;

public class QuanEntryAlreadyExists extends RuntimeException {
    public QuanEntryAlreadyExists(String message) {
        super(message);
    }
}
