package arthur.silva.discordbot.base.shutdown.base;

/**
 * Represents a shutdown module error
 */
public class ShutdownException extends Exception {
    public ShutdownException(String message) {
        super(message);
    }
}
