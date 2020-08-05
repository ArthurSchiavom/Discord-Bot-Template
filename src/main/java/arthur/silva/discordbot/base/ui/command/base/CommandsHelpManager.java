package arthur.silva.discordbot.base.ui.command.base;

import arthur.silva.discordbot.base.infrastructure.application.utils.CollectionUtils;
import arthur.silva.discordbot.base.ui.command.base.formatter.type.CommandHelpFormatter;
import arthur.silva.discordbot.base.ui.command.base.formatter.type.MainMenuFormatter;
import net.dv8tion.jda.api.entities.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Provides all menus related to commands.
 */
@Component
@DependsOn("bootstrap")
public class CommandsHelpManager {
    private final Logger LOGGER = LoggerFactory.getLogger(CommandsHelpManager.class);

    private CommandsManager commandsManager;
    @Autowired private CommandHelpFormatter commandHelpFormatter;
    @Autowired private MainMenuFormatter mainMenuFormatter;

    private final Map<Command, Message> commandToHelpMessageMap = new HashMap<>();
    private final Map<MainMenuType, Message> mainMenuMessages = new HashMap<>();

    public void setup(Map<String, Command> allCommands, Collection<Command> rootCommands, CommandsManager commandsManager) {
        this.commandsManager = commandsManager;
        registerCommandsHelpMenus(allCommands);
        registerMainMenus(rootCommands);
    }

    private void registerCommandsHelpMenus(Map<String, Command> commandsFullNameToCommand) {
        for (Map.Entry<String, Command> commandEntry : commandsFullNameToCommand.entrySet()) {
            String commandFullname = commandEntry.getKey();
            Command command = commandEntry.getValue();

            commandToHelpMessageMap.putIfAbsent(commandEntry.getValue(), commandHelpFormatter.format(commandEntry));
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

        boolean coherent = areMainMenusCommandsCoherent(mainMenuToCommands);
        if (!coherent) {
            LOGGER.warn("There are main menus whose commands overlap");
        }

        return mainMenuToCommands;
    }

    @Nullable
    public Message getHelpMessageFor(String commandUsage, boolean hasPrefix) {
        Command command = commandsManager.findCommand(commandUsage, hasPrefix);
        if (command == null)
            return null;

        return commandToHelpMessageMap.get(command);
    }

    @Nullable
    public Message getHelpMessageFor(Command command) {
        return commandToHelpMessageMap.get(command);
    }

    private boolean areMainMenusCommandsCoherent(Map<MainMenuType, Set<Command>> mainMenusCommands) {
        Collection<? extends Collection<Command>> commands = mainMenusCommands.values();
        Collection[] commandsArray = commands.toArray(new Collection[0]);
        return !CollectionUtils.doCollectionsOverlap(commandsArray);
    }

    public Message getMainMenu(MainMenuType mainMenuType) {
        return mainMenuMessages.get(mainMenuType);
    }
}
