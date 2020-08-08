package discord.bot.ui.events;

import discord.bot.ui.events.operators.GuildMessageEventOperator;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Manages received JDA events.
 */
@Component
public class EventsManager extends ListenerAdapter {

    @Autowired
    private GuildMessageEventOperator guildMessageEventOperator;

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        guildMessageEventOperator.processMessageReceived(event);
    }

}
