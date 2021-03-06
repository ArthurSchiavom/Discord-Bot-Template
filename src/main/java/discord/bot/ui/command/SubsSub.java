package discord.bot.ui.command;

import discord.bot.ui.command.base.Category;
import discord.bot.ui.command.base.CommandUsageExample;
import discord.bot.ui.command.base.CommandWithoutSubcommands;
import discord.bot.ui.events.MessageReceivedEvent;
import org.springframework.stereotype.Component;

@Component
public class SubsSub extends CommandWithoutSubcommands {
    public SubsSub() {
        super(Category.INFORMATION, "daddy?", true, "I", "don't", "have", "args");
        this.registerName("Baby");
        this.registerName("child");
        this.registerName("LittleOne");
        this.registerUsageExample(new CommandUsageExample("a b", "c d"));
        this.registerUsageExample(new CommandUsageExample("e f", "g h"));
    }

    @Override
    protected void runInternal(MessageReceivedEvent event) {
        event.getChannel().sendMessage("buaaaa").queue();
    }
}
