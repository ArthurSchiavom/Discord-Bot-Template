package arthur.silva.discordbot.base.ui.command.base;

import arthur.silva.discordbot.base.ui.command.Help;
import arthur.silva.discordbot.base.ui.command.SubTest;
import arthur.silva.discordbot.base.ui.command.base.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Configuration
public class CommandConfig {

    @Autowired
    private Help help;
    @Autowired
    private SubTest subTest;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean(name="rootCommandsInOrder")
    public Collection<Command> commandsInOrder() {
        Collection<Command> result = new ArrayList<>();
        // *** Register commands here
        result.add(help);
        result.add(subTest);

        return result;
    }
}
