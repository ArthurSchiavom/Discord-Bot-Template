package arthur.silva.discordbot.base.ui.command.base;

import arthur.silva.discordbot.base.ui.command.Help;
import arthur.silva.discordbot.base.ui.command.SubTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
