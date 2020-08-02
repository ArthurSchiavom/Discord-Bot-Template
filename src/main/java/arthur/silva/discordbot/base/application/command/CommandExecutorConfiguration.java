package arthur.silva.discordbot.base.application.command;

import arthur.silva.discordbot.base.ui.command.Help;
import arthur.silva.discordbot.base.ui.command.base.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class CommandExecutorConfiguration {

    @Autowired
    private Help help;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean(name="commandsInOrder")
    public List<Command> commandsInOrder() {
        List<Command> result = new ArrayList<>();
        // *** Register commands here
        result.add(help);

        return result;
    }

    @Bean
    public Help helpCommand() {
        return new Help();
    }
}
