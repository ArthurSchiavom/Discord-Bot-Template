package arthur.silva.discordbot.base.application.events;

import arthur.silva.discordbot.base.application.events.operators.GuildMessageEventOperator;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Manages received JDA events.
 */
public class EventsManager extends ListenerAdapter {

    @Autowired
    private GuildMessageEventOperator guildMessageEventOperator;
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        guildMessageEventOperator.processMessageReceived(event);
    }

}
