package arthur.silva.discordbot.base.ui.command.base;

import arthur.silva.discordbot.base.ui.command.formatter.type.CommandHelpFormatter;
import arthur.silva.discordbot.base.ui.command.formatter.type.MainMenuFormatter;
import net.dv8tion.jda.api.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Provides all menus related to commands.
 */
@Component
public class CommandsHelpManager {

    @Autowired private CommandHelpFormatter commandHelpFormatter;
    @Autowired private MainMenuFormatter mainMenuFormatter;

    private final Map<Command, Message> commandHelpMessages = new HashMap<>();
    private final Map<MainMenuType, Message> mainMenuMessages = new HashMap<>();

    public void setup(Map<String, Command> allCommands, Collection<Command> rootCommands) {
        registerCommandsHelpMenus(allCommands);
        registerMainMenus(rootCommands);
    }

    private void registerCommandsHelpMenus(Map<String, Command> allCommands) {
        for (Map.Entry<String, Command> commandEntry : allCommands.entrySet()) {
            commandHelpMessages.put(commandEntry.getValue(), commandHelpFormatter.format(commandEntry));
        }
    }

    private void registerMainMenus(Collection<Command> rootCommands) {
        Map<MainMenuType, Set<Command>> mainMenuToCommands = associateCommandsToMainMenus(rootCommands);
        registerMainMenus(mainMenuToCommands);
    }

    private void registerMainMenus(Map<MainMenuType, Set<Command>> mainMenuToCommandsMap) {
        for (Map.Entry<MainMenuType, Set<Command>> entry : mainMenuToCommandsMap.entrySet()) {
            Message menu = mainMenuFormatter.format(entry.getValue());
            mainMenuMessages.put(entry.getKey(), menu);
        }
    }

    private Map<MainMenuType, Set<Command>> associateCommandsToMainMenus(Collection<Command> rootCommands) {
        MainMenuType[] menuTypes = MainMenuType.values();
        Map<MainMenuType, Set<Command>> mainMenuToCommands = new LinkedHashMap<>();
        for (Command command : rootCommands) {
            for (MainMenuType menuType : menuTypes) {
                if (menuType.accepts(command)) {
                    Set<Command> commandsOfType = mainMenuToCommands.computeIfAbsent(menuType, m -> new LinkedHashSet<>());
                    commandsOfType.add(command);
                    break;
                }
            }
        }

        return mainMenuToCommands;
    }

    public Message getHelp(Command command) {
        return commandHelpMessages.get(command);
    }


    public Message getMainMenu(MainMenuType mainMenuType) {
        return mainMenuMessages.get(mainMenuType);
    }
}
