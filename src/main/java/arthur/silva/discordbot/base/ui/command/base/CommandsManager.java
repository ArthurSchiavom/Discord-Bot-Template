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

@Component
@DependsOn("bootstrap")
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
        fullNameLowerCaseToCommandMap = retrieveFullNameLowercaseToCommandMapRecursively(this.rootCommandsInOrder, true);
        commandsHelpManager.setup(retrieveFullNameLowercaseToCommandMapRecursively(rootCommandsInOrder, false), rootCommandsInOrder, this);
        maxDepth = calcMaxDepth(fullNameLowerCaseToCommandMap.values());
    }

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

    private Map<String, Command> retrieveFullNameLowercaseToCommandMapRecursively(@NonNull Collection<Command> rootCommands, boolean lowercase) {
        Map<String, Command> result = new LinkedHashMap<>();
        retrieveFullNameLowercaseToCommandMapRecursively(rootCommands, result, "", lowercase, true);
        return result;
    }

    private void retrieveFullNameLowercaseToCommandMapRecursively(@NonNull Collection<Command> commands, @NonNull Map<String, Command> result, @NonNull String nameSoFar, boolean lowercase, boolean isFirst) {
        String thisFullName;
        for (Command thisCommand : commands) {
            for (String thisName : thisCommand.getNames()) {
                thisFullName = calculateFullName(nameSoFar, thisName, !isFirst, lowercase);

                result.put(thisFullName, thisCommand);

                if (thisCommand instanceof CommandWithSubcommands) {
                    CommandWithSubcommands thisCommandWithSubcommands = (CommandWithSubcommands) thisCommand;
                    retrieveFullNameLowercaseToCommandMapRecursively(thisCommandWithSubcommands.getSubcommands(), result, thisFullName, lowercase, false);
                }
            }
        }
    }

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
            String nextSubMessage = StringUtils.joinElements(0, i, GlobalConfiguration.Command.ARGUMENT_SEPARATOR, arguments);
            command = fullNameLowerCaseToCommandMap.get(nextSubMessage);
            if (command != null)
                break;
        }

        return command;
    }

    public static class Utils {
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
