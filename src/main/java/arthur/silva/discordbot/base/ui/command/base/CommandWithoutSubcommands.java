package arthur.silva.discordbot.base.ui.command.base;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * A command without sub-commands
 */
public abstract class CommandWithoutSubcommands extends Command {

    public CommandWithoutSubcommands(Category category, String description, boolean runInNewThread) {
        super(category, description, runInNewThread);
    }
}
