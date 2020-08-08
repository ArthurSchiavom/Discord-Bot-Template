package arthur.silva.discordbot.base.ui.command.base;

import arthur.silva.discordbot.base.data.loaded.configuration.GlobalConfiguration;
import arthur.silva.discordbot.base.infrastructure.application.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Manages a group of commands.
 */
@Component
public class CommandsManager {
    private Set<Command> rootCommandsInOrder;
    private Map<String, Command> fullNameLowerCaseToCommandMap;
    @Autowired private CommandsHelpManager commandsHelpManager;
    @Autowired private ApplicationContext applicationContext;
    int maxDepth;

    @PostConstruct
    private void postInit() {
        Collection<Command> rootCommands = applicationContext.getBean("rootCommandsInOrder", Collection.class);
        this.rootCommandsInOrder = new LinkedHashSet<>(rootCommands);
        fullNameLowerCaseToCommandMap = retrieveFullNameToCommandMapRecursively(this.rootCommandsInOrder, true);
        commandsHelpManager.setup(retrieveFullNameToCommandMapRecursively(rootCommandsInOrder, false), rootCommandsInOrder, this);
        maxDepth = calcMaxDepth(fullNameLowerCaseToCommandMap.values());
    }

    /**
     * Calculates the max depth of a collection of commands.
     *
     * @param commands the commands to analyse
     * @return max depth
     */
    private int calcMaxDepth(@NonNull Collection<Command> commands) {
        int max = 0;
        for (Command command : commands) {
            int current = command.getDepth();
            if (current > max) {
                max = current;
            }
        }

        return max;
    }

    /**
     * Calculates a map of commands' full name mapped to their respective command.
     *
     * @param rootCommands commands without supercommands
     * @param lowercase if the names should be in lowercase
     * @return map of commands' full name mapped to their respective command
     */
    private Map<String, Command> retrieveFullNameToCommandMapRecursively(@NonNull Collection<Command> rootCommands, boolean lowercase) {
        Map<String, Command> result = new LinkedHashMap<>();
        retrieveFullNameToCommandMapRecursively(rootCommands, result, "", lowercase, true);
        return result;
    }

    /**
     * Calculates a map of commands' full name mapped to their respective command.
     *
     * @param commands commands without supercommands to analyse
     * @param result result so far
     * @param nameSoFar name according to depth
     * @param lowercase if the resulting name should be in lowercase
     * @param isFirst if it's the root level
     */
    private void retrieveFullNameToCommandMapRecursively(@NonNull Collection<Command> commands, @NonNull Map<String, Command> result, @NonNull String nameSoFar, boolean lowercase, boolean isFirst) {
        String thisFullName;
        for (Command thisCommand : commands) {
            for (String thisName : thisCommand.getNames()) {
                thisFullName = calculateFullName(nameSoFar, thisName, !isFirst, lowercase);

                result.put(thisFullName, thisCommand);

                if (thisCommand instanceof CommandWithSubcommands) {
                    CommandWithSubcommands thisCommandWithSubcommands = (CommandWithSubcommands) thisCommand;
                    retrieveFullNameToCommandMapRecursively(thisCommandWithSubcommands.getSubcommands(), result, thisFullName, lowercase, false);
                }
            }
        }
    }

    /**
     * Calculates a command's full name. This is a helper method.
     *
     * @param nameSoFar the name so far down the chain
     * @param name this command's name
     * @param includeSeparator if the argument separator should be included before this command's name
     * @param lowercase if the name should be in lowercase
     * @return the calculated full name
     */
    private String calculateFullName(@NonNull String nameSoFar, @NonNull String name, boolean includeSeparator, boolean lowercase) {
        StringBuilder sb = new StringBuilder();

        sb.append(nameSoFar);
        if (includeSeparator) {
            sb.append(GlobalConfiguration.Command.ARGUMENT_SEPARATOR);
        }

        sb.append(name);
        String fullName = sb.toString();
        if (lowercase) {
            fullName = fullName.toLowerCase();
        }

        return fullName;
    }

    /**
     * Attempts to find an issued command in the user message.
     *
     * @param userMessage possible command
     * @param hasPrefix if the prefix is included
     * @return (1) the command if this message is issuing one or (2) null if the message doesn't issue a command
     */
    @Nullable
    public Command findCommand(@NonNull String userMessage, boolean hasPrefix) {
        if (hasPrefix)
            userMessage = Command.removePrefix(userMessage);

        if (userMessage.isEmpty()) {
            return null;
        }
        userMessage = userMessage.toLowerCase();
        String[] arguments = userMessage.split(GlobalConfiguration.Command.ARGUMENT_SEPARATOR);
        int thisMaxDepth = Math.min(arguments.length, maxDepth);

        Command command = null;
        for (int i = thisMaxDepth - 1; i >= 0; i--) {
            String nextSubMessage = StringUtils.joinElements(0, i, GlobalConfiguration.Command.ARGUMENT_SEPARATOR, GlobalConfiguration.Command.ARGUMENT_SEPARATOR, arguments);
            command = fullNameLowerCaseToCommandMap.get(nextSubMessage);
            if (command != null)
                break;
        }

        return command;
    }

    /**
     * Utils related to commands' management.
     */
    public static class Utils {
        /**
         * Splits commands by their category.
         *
         * @param commands commands to split
         * @return commands by category
         */
        public static Map<Category, Set<Command>> splitCommandsByCategory(@NonNull Collection<Command> commands) {
            Map<Category, Set<Command>> result = new LinkedHashMap<>();

            for (Command command : commands) {
                Category category = command.getCategory();
                Set<Command> resultCommands = result.computeIfAbsent(category, k -> new LinkedHashSet<>());
                resultCommands.add(command);
            }

            return result;
        }
    }
}
