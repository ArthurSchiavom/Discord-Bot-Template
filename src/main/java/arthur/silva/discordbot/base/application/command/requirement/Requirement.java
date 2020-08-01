package arthur.silva.discordbot.base.application.command.requirement;

import arthur.silva.discordbot.base.application.events.MessageReceivedEvent;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

/**
 * Requirement for a command to be accessible.
 */
public enum Requirement {
	SAME_VOICE_CHANNEL (event -> {
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
	})
	, GUILD_HAS_MUSIC_MANAGER_ACTIVE(event -> {
//		TODO
//		GuildMusicManager musicManager = PlayerManager.getInstance().getGuildMusicManager(event.getGuild().getIdLong());
//
//		if (musicManager == null) {
//			event.getChannel().sendMessage("**There's nothing playing.**").queue();
//			return false;
//		}

		throw new UnsupportedOperationException("This feature was not implemented yet");
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
