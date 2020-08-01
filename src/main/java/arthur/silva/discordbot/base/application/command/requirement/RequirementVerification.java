package arthur.silva.discordbot.base.application.command.requirement;


import arthur.silva.discordbot.base.application.events.MessageReceivedEvent;

/**
 * Callback for an operation that verifies if a given event meets a requirement.
 */
public interface RequirementVerification {
	/**
	 * Verifies if an event meets this requirement.
	 *
	 * @param event the event to verify
	 * @return the verification's result
	 */
	RequirementVerificationResult meetsRequirement(MessageReceivedEvent event);
}
