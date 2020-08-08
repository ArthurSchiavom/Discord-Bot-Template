package discord.bot.ui.command.base;

import discord.bot.ui.command.Help;
import discord.bot.ui.command.SubTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Commands' configuration.
 */
@Configuration
public class CommandConfig {

    @Autowired
    private Help help;
    @Autowired
    private SubTest subTest;

    @Bean(name="rootCommandsInOrder")
    public Collection<Command> commandsInOrder() {
        Collection<Command> result = new ArrayList<>();
        // *** Register commands here
        result.add(help);
        result.add(subTest);

        return result;
    }
}
