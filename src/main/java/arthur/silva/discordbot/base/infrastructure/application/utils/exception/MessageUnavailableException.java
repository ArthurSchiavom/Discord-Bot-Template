package arthur.silva.discordbot.base.infrastructure.application.utils.exception;

/**
 * Indicates that the message wasn't available for the bot account to access.
 */
public class MessageUnavailableException extends Exception {
	public MessageUnavailableException(String message) {
		super(message);
	}
}
