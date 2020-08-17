package discord.bot.boot_shutdown.bootstrap.boostrappers;

/**
 * Notification of a bootstrapping error.
 */
public class BootstrapException extends Exception {

    public final boolean shouldExit;

    public BootstrapException(String message, boolean shouldExit) {
        super(message);
        this.shouldExit = shouldExit;
    }

}
