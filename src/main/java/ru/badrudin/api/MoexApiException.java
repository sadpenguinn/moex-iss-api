package ru.badrudin.api;

public class MoexApiException extends Exception {
    public MoexApiException (String message) {
        super (message);
    }

    public MoexApiException (Throwable cause) {
        super (cause);
    }
}
