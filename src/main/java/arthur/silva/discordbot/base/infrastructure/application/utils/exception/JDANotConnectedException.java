package arthur.silva.discordbot.base.infrastructure.application.utils.exception;

/**
 * This means that the JDA is not connected to Discord.
 */
public class JDANotConnectedException extends Exception {
	public JDANotConnectedException(String message) {
		super(message);
	}
}
