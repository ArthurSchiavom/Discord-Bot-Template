package discord.bot.ui.command.base;

import discord.bot.ui.events.MessageReceivedEvent;
import net.dv8tion.jda.api.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A command with sub-commands
 */
public abstract class CommandWithSubcommands extends Command {
    private final Set<Command> subcommands = new LinkedHashSet<>();

    @Autowired
    private CommandsHelpManager helpManager;

    public CommandWithSubcommands(@NonNull Category category, @NonNull String description) {
        super(category, description, false);
    }

    /**
     * Register commands as sub-commands of this command.
     *
     * @param commands commands to register.
     */
    protected void registerSubcommands(@NonNull Command... commands) {
        for (Command command : commands) {
            subcommands.add(command);
            command.incrementDepth();
        }
    }

    /**
     * This command's specific behaviour.
     *
     * @param event the event that triggered the command
     */
    @Override
    protected final void runInternal(@NonNull MessageReceivedEvent event) {
        Message reply = helpManager.getHelpMessageFor(this);
        event.getChannel().sendMessage(reply).queue();
    }

    /**
     * @return this command's subcommands
     */
    public Set<Command> getSubcommands() {
        return new LinkedHashSet<>(subcommands);
    }
}
