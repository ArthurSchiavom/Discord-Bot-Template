package arthur.silva.discordbot.base.ui.command.base;

import arthur.silva.discordbot.base.application.events.MessageReceivedEvent;
import arthur.silva.discordbot.base.ui.command.Help;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A command with sub-commands
 */
public abstract class CommandWithSubcommands extends Command {
    private final Set<Command> subcommands = new LinkedHashSet<>();

    @Autowired
    private Help help;

    public CommandWithSubcommands(@NonNull Category category, @NonNull String description) {
        super(category, description, false, "[Option]");
    }

    protected void registerSubcommands(@NonNull Command... commands) {
        for (Command command : commands) {
            registerSubcommand(command);
        }
    }

    protected void registerSubcommand(@NonNull Command command) {
        subcommands.add(command);
    }

    @Override
    protected void runInternal(@NonNull MessageReceivedEvent event, int nCommandsInChain) {
        nCommandsInChain++;
        String currentArgs = this.removePrefix(event.getMessage().getContentDisplay());
        String[] split = currentArgs.split(Command.ARGUMENT_SEPARATOR);
        boolean isSubcommand = false;
        if (split.length > nCommandsInChain) {
            String possibleCommandName = split[nCommandsInChain];
            Command possibleCommand = searchForSubcommand(possibleCommandName);
            if (possibleCommand != null) {
                possibleCommand.run(event, nCommandsInChain);
                isSubcommand = true;
            }
        }

        if (!isSubcommand)
            event.getChannel().sendMessage(help.getHelpFor(this)).queue();
    }

    @Nullable
    private Command searchForSubcommand(@NonNull String nameToSearchFor) {
        for (Command command : subcommands) {
            for (String name : command.getNames()) {
                if (name.equalsIgnoreCase(nameToSearchFor)) {
                    return command;
                }
            }
        }

        return null;
    }

    public Set<Command> getSubcommands() {
        return new LinkedHashSet<>(subcommands);
    }
}
