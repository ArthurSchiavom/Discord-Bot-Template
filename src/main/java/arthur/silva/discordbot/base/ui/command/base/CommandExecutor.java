package arthur.silva.discordbot.base.ui.command.base;

import arthur.silva.discordbot.base.application.events.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

@Component
public class CommandExecutor {
    private CommandsManager commandsManager;
    @Autowired ApplicationContext applicationContext;

    @PostConstruct
    private void init() {
        Collection<Command> commands = applicationContext.getBean("commandsInOrder", Collection.class);
        commandsManager = applicationContext.getBean(CommandsManager.class, commands);
    }

    public boolean executePossibleCommand(MessageReceivedEvent event) {
        String message = event.getMessage().getContentDisplay();
        Command command = commandsManager.findCommand(message);
        if (command == null)
            return false;

        command.run(event);
        return true;
    }
}
