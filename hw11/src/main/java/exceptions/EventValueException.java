package exceptions;

public class EventValueException extends Exception {
    public EventValueException(String message) {
        super("Event value exception: " + message);
    }
}
