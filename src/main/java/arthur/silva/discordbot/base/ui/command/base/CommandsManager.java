package arthur.silva.discordbot.base.ui.command.base;

import arthur.silva.discordbot.base.data.loaded.configuration.GlobalConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class CommandsManager {
    private final Set<Command> rootCommands;
    private final Map<String, Command> allCommandsFullNameWithPrefixLowerCase;
    @Autowired private CommandsHelpManager commandsHelpManager;

    public CommandsManager(Command... rootCommands) {
        Collection<Command> commandCollection = Arrays.asList(rootCommands);
        this.rootCommands = new LinkedHashSet<>(commandCollection);
        allCommandsFullNameWithPrefixLowerCase = retrieveAllCommandsByFullNameWithPrefixRecursively(this.rootCommands, true);
    }

    @PostConstruct
    private void postInit() {
        commandsHelpManager.setup(retrieveAllCommandsByFullNameWithPrefixRecursively(rootCommands, false), rootCommands);
    }

    private Map<String, Command> retrieveAllCommandsByFullNameWithPrefixRecursively(Collection<Command> rootCommands, boolean lowercase) {
        Map<String, Command> result = new LinkedHashMap<>();
        retrieveAllCommandsByFullNameWithPrefixRecursively(rootCommands, result, GlobalConfiguration.Command.commandPrefix, lowercase, true);
        return result;
    }

    private void retrieveAllCommandsByFullNameWithPrefixRecursively(Collection<Command> commands, Map<String, Command> result, String nameSoFar, boolean lowercase, boolean isFirst) {
        String thisFullName;
        StringBuilder sb;
        for (Command thisCommand : commands) {
            for (String thisName : thisCommand.getNames()) {
                thisFullName = calculateFullNameWithPrefix(nameSoFar, thisName, !isFirst, lowercase);

                result.put(thisFullName, thisCommand);

                if (thisCommand instanceof CommandWithSubcommands) {
                    CommandWithSubcommands thisCommandWithSubcommands = (CommandWithSubcommands) thisCommand;
                    retrieveAllCommandsByFullNameWithPrefixRecursively(thisCommandWithSubcommands.getSubcommands(), result, thisFullName, lowercase, false);
                }
            }
        }
    }

    private String calculateFullNameWithPrefix(String nameSoFar, String name, boolean includeSeparator, boolean lowercase) {
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

    public Command findCommand(String userMessage) {
        userMessage = userMessage.toLowerCase();
        for (Map.Entry<String, Command> entry : allCommandsFullNameWithPrefixLowerCase.entrySet()) {
            if (userMessage.startsWith(entry.getKey()))
                return entry.getValue();
        }
        return null;
    }

    public static class Utils {
        /**
         *
         * @param commands
         * @return every set has at least one command. Empty sets are omitted
         */
        public static Map<Category, Set<Command>> splitCommandsByCategory(Collection<Command> commands) {
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
