package arthur.silva.discordbot.base.ui.command;

import arthur.silva.discordbot.base.application.events.MessageReceivedEvent;
import arthur.silva.discordbot.base.ui.command.base.Category;
import arthur.silva.discordbot.base.ui.command.base.CommandUsageExample;
import arthur.silva.discordbot.base.ui.command.base.CommandWithoutSubcommands;
import org.springframework.stereotype.Component;

@Component
public class SubsSub extends CommandWithoutSubcommands {
    public SubsSub() {
        super(Category.INFORMATION, "daddy?", true, "I don't really have args");
        this.registerName("Baby");
        this.registerName("child");
        this.registerName("LittleOne");
        this.registerUsageExample(new CommandUsageExample("a b", "c d"));
        this.registerUsageExample(new CommandUsageExample("e f", "g h"));
    }

    /**
     * Execute this particular command's actions.
     *
     * @param event the event that triggered the command
     */
    @Override
    protected void runInternal(MessageReceivedEvent event) {
        event.getChannel().sendMessage("buaaaa").queue();
    }
}
