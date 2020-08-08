package discord.bot.shutdown;

/**
 * Represents a shutdown module error
 */
public class ShutdownException extends Exception {
    public ShutdownException(String message) {
        super(message);
    }
}
