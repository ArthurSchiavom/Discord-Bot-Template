package arthur.silva.discordbot.base.ui.command.formatter.base;

import arthur.silva.discordbot.base.ui.command.base.Command;
import net.dv8tion.jda.api.entities.Message;

public interface CommandHelpFormatter {
    Message format(Command command);
}
