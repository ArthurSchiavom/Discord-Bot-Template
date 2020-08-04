package arthur.silva.discordbot.base.ui.command.base;

import arthur.silva.discordbot.base.ui.command.base.requirement.Requirement;
import arthur.silva.discordbot.base.data.loaded.configuration.CommandUserConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
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
        allCommandsFullNameWithPrefixLowerCase = allCommandsByFullNameWithPrefixRecursively(this.rootCommands, true);
    }

    @PostConstruct
    private void postInit() {
        commandsHelpManager.setup(allCommandsByFullNameWithPrefixRecursively(rootCommands, false), rootCommands);
    }

    private Map<String, Command> allCommandsByFullNameWithPrefixRecursively(Collection<Command> rootCommands, boolean lowercase) {
        Map<String, Command> result = new LinkedHashMap<>();
        allCommandsByFullNameWithPrefixRecursively(rootCommands, result, CommandUserConfig.commandPrefix, lowercase, true);
        return result;
    }

    private void allCommandsByFullNameWithPrefixRecursively(Collection<Command> commands, Map<String, Command> result, String nameSoFar, boolean lowercase, boolean isFirst) {
        String thisFullName;
        StringBuilder sb;
        for (Command thisCommand : commands) {
            for (String thisName : thisCommand.getNames()) {
                thisFullName = calculateFullNameWithPrefix(nameSoFar, thisName, !isFirst, lowercase);

                result.put(thisFullName, thisCommand);

                if (thisCommand instanceof CommandWithSubcommands) {
                    CommandWithSubcommands thisCommandWithSubcommands = (CommandWithSubcommands) thisCommand;
                    allCommandsByFullNameWithPrefixRecursively(thisCommandWithSubcommands.getSubcommands(), result, thisFullName, lowercase, false);
                }
            }
        }
    }

    private String calculateFullNameWithPrefix(String nameSoFar, String name, boolean includeSeparator, boolean lowercase) {
        StringBuilder sb = new StringBuilder();

        sb.append(nameSoFar);
        if (includeSeparator) {
            sb.append(CommandUserConfig.ARGUMENT_SEPARATOR);
        }

        sb.append(name);
        String fullName = sb.toString();
        if (lowercase) {
            fullName = fullName.toLowerCase();
        }

        return fullName;
    }

    private Map<Category, Set<Command>> retrieveAllCommandsByCategoryWithRequirements(@Nullable Requirement[] mustHaveRequirements, @Nullable Requirement[] mayAlsoHaveRequirements) {
        Map<Category, Set<Command>> result = retrieveAllCommandsByCategoryRecursively();

        for (Map.Entry<Category, Set<Command>> entry : retrieveAllCommandsByCategoryRecursively ().entrySet()) {
            Set<Command> commandsToRemove = new LinkedHashSet<>();
            for (Command command : entry.getValue()) {
                if (!command.getRequirementsManager().requiresOnly(mustHaveRequirements, mayAlsoHaveRequirements)) {
                    commandsToRemove.add(command);
                }
            }

            for (Command command : commandsToRemove) {
                entry.getValue().remove(command);
            }

            if (entry.getValue().size() == 0)
                result.remove(entry.getKey());
        }

        return result;
    }

    public Set<Command> retrieveAllCommandsWithRequirementsRecursively(@Nullable Requirement[] mustHaveRequirements, @Nullable Requirement[] mayAlsoHaveRequirements) {
        Set<Command> result = new LinkedHashSet<>();
        for (Command command : retrieveAllCommandsRecursively()) {
            if (command.getRequirementsManager().requiresOnly(mustHaveRequirements, mayAlsoHaveRequirements))
                result.add(command);
        }
        return result;
    }

    public Map<Category, Set<Command>> retrieveAllCommandsByCategoryRecursively() {
        Map<Category, Set<Command>> result = new LinkedHashMap<>();

        for (Command command : retrieveAllCommandsRecursively()) {
            Category category = command.getCategory();
            Set<Command> resultCommands = result.computeIfAbsent(category, k -> new LinkedHashSet<>());
            resultCommands.add(command);
        }

        return result;
    }

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

    public Set<Command> retrieveAllCommandsRecursively() {
        Set<Command> result = new LinkedHashSet<>();
        retrieveAllCommandsRecursively(result, rootCommands);
        return result;
    }

    private void retrieveAllCommandsRecursively(Set<Command> result, Collection<Command> commandsToProcess) {
        for (Command command : commandsToProcess) {
            result.add(command);
            if (command instanceof CommandWithSubcommands) {
                CommandWithSubcommands commandWithSubcommands = (CommandWithSubcommands) command;
                retrieveAllCommandsRecursively(result, commandWithSubcommands.getSubcommands());
            }
        }
    }

    public Command findCommand(String userMessage) {
        userMessage = userMessage.toLowerCase();
        for (Map.Entry<String, Command> entry : allCommandsFullNameWithPrefixLowerCase.entrySet()) {
            if (userMessage.startsWith(entry.getKey()))
                return entry.getValue();
        }
        return null;
    }
}
