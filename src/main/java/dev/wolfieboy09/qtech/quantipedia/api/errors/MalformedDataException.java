package dev.wolfieboy09.qtech.quantipedia.api.errors;

public class MalformedDataException extends RuntimeException {
    public MalformedDataException(String message) {
        super(message);
    }
}
