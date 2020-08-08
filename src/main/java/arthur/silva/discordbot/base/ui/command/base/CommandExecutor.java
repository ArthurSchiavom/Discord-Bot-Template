package arthur.silva.discordbot.base.ui.command.base;

import arthur.silva.discordbot.base.ui.events.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * Executes commands.
 */
@Component
public class CommandExecutor {
    @Autowired private CommandsManager commandsManager;
    @Autowired private ApplicationContext applicationContext;

    public boolean executePossibleCommand(MessageReceivedEvent event) {
        String message = event.getMessage().getContentDisplay();
        Command command = commandsManager.findCommand(message, true);
        if (command == null)
            return false;

        command.run(event);
        return true;
    }
}
