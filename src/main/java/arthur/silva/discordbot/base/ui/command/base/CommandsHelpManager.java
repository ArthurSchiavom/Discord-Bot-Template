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
public class CommandsHelpManager {
    private final Logger LOGGER = LoggerFactory.getLogger(CommandsHelpManager.class);

    private CommandsManager commandsManager;
    @Autowired private CommandHelpFormatter commandHelpFormatter;
    @Autowired private MainMenuFormatter mainMenuFormatter;

    private final Map<Command, Message> commandToHelpMessageMap = new HashMap<>();
    private final Map<MainMenuType, Message> mainMenuMessages = new HashMap<>();

    /**
     * Sets up this instance, preparing it to be used. This method must be ran before using the other methods.
     *
     * @param allCommands map of a command's full name to the command itself. Includes all subcommands
     * @param rootCommands commands of depth 1, meaning they have no supercommand
     * @param commandsManager commands' manager
     */
    public void setup(Map<String, Command> allCommands, Collection<Command> rootCommands, CommandsManager commandsManager) {
        this.commandsManager = commandsManager;
        registerCommandsHelpMenus(allCommands);
        registerMainMenus(rootCommands);
    }

    /**
     * Registers individual commands' help menus.
     *
     * @param commandsFullNameToCommand commands to process
     */
    private void registerCommandsHelpMenus(Map<String, Command> commandsFullNameToCommand) {
        for (Map.Entry<String, Command> commandEntry : commandsFullNameToCommand.entrySet()) {
            String commandFullname = commandEntry.getKey();
            Command command = commandEntry.getValue();

            commandToHelpMessageMap.putIfAbsent(commandEntry.getValue(), commandHelpFormatter.format(commandEntry));
        }
    }

    /**
     * Registers commands' main menus.
     *
     * @param rootCommands commands without supercommand
     */
    private void registerMainMenus(Collection<Command> rootCommands) {
        Map<MainMenuType, Set<Command>> mainMenuToCommands = associateCommandsToMainMenus(rootCommands);
        registerMainMenus(mainMenuToCommands);
    }

    /**
     * Registers commands' main menus.
     *
     * @param mainMenuToCommandsMap commands organized by menu type
     */
    private void registerMainMenus(Map<MainMenuType, Set<Command>> mainMenuToCommandsMap) {
        for (Map.Entry<MainMenuType, Set<Command>> entry : mainMenuToCommandsMap.entrySet()) {
            Message menu = mainMenuFormatter.format(entry.getValue());
            mainMenuMessages.put(entry.getKey(), menu);
        }
    }

    /**
     * Organizes commands by menu type.
     *
     * @param rootCommands commands without supercommand
     * @return commands organized by menu type
     */
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

    /**
     * Retrieves the help message.
     *
     * @param commandUsage the user message that starts with the command name
     * @param hasPrefix if the message includes the commands' prefix
     * @return (1) the help message for the requested command or (2) null if there is no such command
     */
    @Nullable
    public Message getHelpMessageFor(String commandUsage, boolean hasPrefix) {
        Command command = commandsManager.findCommand(commandUsage, hasPrefix);
        if (command == null)
            return null;

        return commandToHelpMessageMap.get(command);
    }

    /**
     * Retrieves the help message for a given command.
     *
     * @param command the command to find the help message for
     * @return the help message for the given command
     */
    @Nullable
    public Message getHelpMessageFor(Command command) {
        return commandToHelpMessageMap.get(command);
    }

    /**
     * Verifies if the main menus are coherent, since their configuration can be tricky.
     * <br>Verifications made: no menus contain the same element
     *
     * @param mainMenusCommands the commands per menu type
     * @return true in case of success and false otherwise
     */
    private boolean areMainMenusCommandsCoherent(Map<MainMenuType, Set<Command>> mainMenusCommands) {
        Collection<? extends Collection<Command>> commands = mainMenusCommands.values();
        Collection[] commandsArray = commands.toArray(new Collection[0]);
        return !CollectionUtils.doCollectionsOverlap(commandsArray);
    }

    /**
     * @return the main menu of a given menu type
     */
    public Message getMainMenu(MainMenuType mainMenuType) {
        return mainMenuMessages.get(mainMenuType);
    }
}
