package arthur.silva.discordbot.base.application.events.operators;

import arthur.silva.discordbot.base.ui.command.base.CommandExecutor;
import arthur.silva.discordbot.base.application.events.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * Guild message events processor
 */
@Component
public class GuildMessageEventOperator implements EventOperator<GuildMessageReceivedEvent> {

	@Autowired
	CommandExecutor commandExecutor;

	/**
	 * Processes a GuildMessageReceivedEvent event.
	 *
	 * @param event The event to process.
	 */
	public void processMessageReceived(GuildMessageReceivedEvent event) {
		if (event.getAuthor().isBot())
			return;

		MessageReceivedEvent messageReceivedEvent = new MessageReceivedEvent(event);
		commandExecutor.executePossibleCommand(messageReceivedEvent);
	}
}
