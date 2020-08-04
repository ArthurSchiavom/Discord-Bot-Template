package arthur.silva.discordbot.base.ui.command.base;

import arthur.silva.discordbot.base.application.events.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandExecutor {
    @Autowired private CommandsManager commandsManager;

    public boolean executePossibleCommand(MessageReceivedEvent event) {
        String message = event.getMessage().getContentDisplay();
        Command command = commandsManager.findCommand(message);
        if (command == null)
            return false;

        command.run(event);
        return true;
    }
}
