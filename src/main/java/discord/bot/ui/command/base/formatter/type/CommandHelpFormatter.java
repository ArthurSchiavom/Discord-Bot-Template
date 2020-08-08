package discord.bot.ui.command.base.formatter.type;

import discord.bot.ui.command.base.Command;
import discord.bot.ui.command.base.formatter.base.Formatter;

import java.util.Map;

/**
 * Formatter for commands' help messages.
 */
public interface CommandHelpFormatter extends Formatter<Map.Entry<String, Command>> {
}
