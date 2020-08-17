package discord.bot.ui.events.operators;

import discord.bot.ui.command.base.CommandExecutor;
import discord.bot.ui.events.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
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
	@Override
	public void processMessageReceived(GuildMessageReceivedEvent event) {
		if (event.getAuthor().isBot())
			return;

		MessageReceivedEvent messageReceivedEvent = new MessageReceivedEvent(event);
		commandExecutor.executePossibleCommand(messageReceivedEvent);
	}
}
