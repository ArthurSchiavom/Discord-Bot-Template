package arthur.silva.discordbot.base.ui.command.base;

/**
 * A command without sub-commands
 */
public abstract class CommandWithoutSubcommands extends Command {
    public CommandWithoutSubcommands(Category category, String description, boolean runInNewThread, String... arguments) {
        super(category, description, runInNewThread, arguments);
    }
}
