package discord.bot.ui.command.base.requirement;

import discord.bot.ui.events.MessageReceivedEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

/**
 * Requirement for a command to be accessible.
 */
public enum Requirement {
	ADMIN (event -> {
		Member member = event.getMember();
		if (member == null)
			return new RequirementVerificationResult(false, "This command can only be used in a server.");

		if (member.hasPermission(Permission.ADMINISTRATOR)) {
			return new RequirementVerificationResult(true, null);
		}
		else {
			return new RequirementVerificationResult(false, "Sorry! You must be an admin to use this command. \uD83D\uDE41");
		}
	})

	, SAME_VOICE_CHANNEL (event -> {
		AudioManager audioManager = event.getGuild().getAudioManager();

		if (!audioManager.isConnected()) {
			return new RequirementVerificationResult(false, "I'm not connected to a voice channel!");
		}

		VoiceChannel voiceChannel = audioManager.getConnectedChannel();
		if (!voiceChannel.getMembers().contains(event.getMember())) {
			return new RequirementVerificationResult(false, "You have to be in the same voice channel as me to use this command!");
		}

		return new RequirementVerificationResult(true, null);
	})

	, BOT_CONNECTED_TO_VOICE_CHANNEL(event -> {
		AudioManager audioManager = event.getGuild().getAudioManager();
		if (!audioManager.isConnected()) {
			return new RequirementVerificationResult(false, "I'm not connected to a voice channel!");
		}

		return new RequirementVerificationResult(true, null);
	});

	private final RequirementVerification requirementVerification;

	/**
	 * @param requirementVerification how to validate if an event meets the requirement
	 */
	Requirement(RequirementVerification requirementVerification) {
		this.requirementVerification = requirementVerification;
	}

	/**
	 * Verifies if this requirement is met by an event.
	 *
	 * @param event the event to analyze
	 * @return the verification's result
	 */
	public RequirementVerificationResult meetsRequirements(MessageReceivedEvent event) {
		return requirementVerification.meetsRequirement(event);
	}
}
