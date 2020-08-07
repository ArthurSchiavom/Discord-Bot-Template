package arthur.silva.discordbot.base.ui.command.base.formatter.type;

import arthur.silva.discordbot.base.ui.command.base.Command;
import arthur.silva.discordbot.base.ui.command.base.formatter.base.Formatter;

import java.util.Map;

/**
 * Formatter for commands' help messages.
 */
public interface CommandHelpFormatter extends Formatter<Map.Entry<String, Command>> {
}
