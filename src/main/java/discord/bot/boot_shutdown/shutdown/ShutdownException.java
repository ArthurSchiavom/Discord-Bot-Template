package discord.bot.boot_shutdown.shutdown;

/**
 * Represents a shutdown module error
 */
public class ShutdownException extends Exception {
    public ShutdownException(String message) {
        super(message);
    }
}
