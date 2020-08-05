package arthur.silva.discordbot.base.ui.command.base;

import arthur.silva.discordbot.base.application.events.MessageReceivedEvent;
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

    protected void registerSubcommands(@NonNull Command... commands) {
        for (Command command : commands) {
            subcommands.add(command);
            command.incrementDepth();
        }
    }

    @Override
    protected void runInternal(@NonNull MessageReceivedEvent event) {
        String message = event.getMessage().getContentDisplay();
        Message reply = helpManager.getHelpMessageFor(this);
        event.getChannel().sendMessage(reply).queue();
    }

    public Set<Command> getSubcommands() {
        return new LinkedHashSet<>(subcommands);
    }
}
