package discord.bot.ui.command.base.formatter;

import discord.bot.data.configuration.GlobalConfiguration;
import discord.bot.infrastructure.application.utils.StringUtils;
import discord.bot.ui.command.base.Command;
import discord.bot.ui.command.base.CommandUsageExample;
import discord.bot.ui.command.base.CommandWithSubcommands;
import discord.bot.ui.command.base.CommandWithoutSubcommands;
import discord.bot.ui.command.base.formatter.type.CommandHelpFormatter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Formatter for commands' help messages.
 */
public class DefaultCommandFormatter implements CommandHelpFormatter {
    /**
     * Generates the help message for a given command and respective command full name.
     *
     * @param commandEntry command and respective command full name
     * @return help message for the given command and respective command full name
     */
    @Override
    public Message format(Map.Entry<String, Command> commandEntry) {
        Command command = commandEntry.getValue();
        String fullName = commandEntry.getKey();

        if (command instanceof CommandWithoutSubcommands)
            return formatSpecific((CommandWithoutSubcommands) command, fullName);
        if (command instanceof CommandWithSubcommands)
            return formatSpecific((CommandWithSubcommands) command, fullName);

        return new MessageBuilder("There is no formatter for this type of command.").build();
    }

    private Message formatSpecific(CommandWithoutSubcommands command, String fullName) {
        return new CommandWithoutSubcommandsHelpGenerator().generate(command, fullName);
    }

    private Message formatSpecific(CommandWithSubcommands command, String fullName) {
        return new CommandWithSubcommandsHelpGenerator().generate(command, fullName);
    }

}

class CommandWithSubcommandsHelpGenerator {

    public Message generate(CommandWithSubcommands command, String fullName) {
        MessageEmbed embed = generateEmbedBuilder(command, fullName).build();
        return new MessageBuilder().setEmbed(embed).build();
    }

    public EmbedBuilder generateEmbedBuilder(CommandWithSubcommands command, String fullName) {
        EmbedBuilder eb = new EmbedBuilder();
        generateEmbedBuilder(eb, command, fullName);
        return eb;
    }

    public void generateEmbedBuilder(EmbedBuilder eb, CommandWithSubcommands command, String fullName) {
        setHeader(eb, command, fullName);
        CommandHelpGenerator.setAliases(eb, command);
        CommandHelpGenerator.setColor(eb);
        CommandHelpGenerator.setExamples(eb, command, fullName);
        setFooter(eb, command, fullName);
    }

    /**
     * Sets both the title and the description.
     */
    private void setHeader(EmbedBuilder eb, CommandWithSubcommands command, String fullName) {
        String title = GlobalConfiguration.Command.commandPrefix + fullName + " [Option]";

        StringBuilder descriptionSb = new StringBuilder();
        descriptionSb.append("**").append(command.getDescription()).append("**\n");
        for (Command subcommand : command.getSubcommands()) {
            descriptionSb.append("\n• ").append(subcommand.getNames().get(0));
        }

        eb.addField(title, descriptionSb.toString(), false);
    }

    private void setFooter(EmbedBuilder eb, CommandWithSubcommands command, String fullName) {
        StringBuilder sb = new StringBuilder();
        String subcommandDefaultName = command.getSubcommands().iterator().next().getDefaultName();
        sb.append("Example of option selection: ")
                .append(GlobalConfiguration.Command.commandPrefix).append(fullName)
                .append(GlobalConfiguration.Command.ARGUMENT_SEPARATOR)
                .append(subcommandDefaultName);
        eb.setFooter(sb.toString());
    }
}

class CommandWithoutSubcommandsHelpGenerator {
    public Message generate(Command command, String fullName) {
        MessageEmbed embed = generateEmbedBuilder(command, fullName).build();
        return new MessageBuilder().setEmbed(embed).build();
    }

    public EmbedBuilder generateEmbedBuilder(Command command, String fullName) {
        EmbedBuilder eb = new EmbedBuilder();
        generateEmbedBuilder(eb, command, fullName);
        return eb;
    }
    
    public void generateEmbedBuilder(EmbedBuilder eb, Command command, String fullName) {
        setHeader(eb, command, fullName);
        CommandHelpGenerator.setColor(eb);
        CommandHelpGenerator.setExamples(eb, command, fullName);
        CommandHelpGenerator.setAliases(eb, command);
    }
    
    private void setHeader(EmbedBuilder eb, Command command, String fullName) {
        StringBuilder headerSb = new StringBuilder();
        headerSb.append(GlobalConfiguration.Command.commandPrefix)
                .append(fullName);

        String argumentsDisplay = StringUtils.generateDisplayList(" ", " ", command.getArguments());
        if (!argumentsDisplay.isEmpty()) {
            headerSb.append(" *").append(argumentsDisplay).append("*");
        }
        String header = headerSb.toString();
        eb.setTitle(header);
        eb.setDescription("**" + command.getDescription() + "**");
    }
}

/**
 * Used by all generators.
 */
class CommandHelpGenerator {
    
    public static void setColor(EmbedBuilder eb) {
        eb.setColor(GlobalConfiguration.Command.defaultEmbedColor);
    }

    public static void setAliases(EmbedBuilder eb, Command command) {
        List<String> aliases = command.getNames();
        if (aliases.size() < 2) {
            return;
        }

        aliases.remove(0);
        String helpAliases = StringUtils.generateDisplayList(", ", ", ", aliases);
        eb.addField("Aliases", helpAliases, false);
    }

    public static void setExamples(EmbedBuilder eb, Command command, String fullName) {
        if (command.getUsageExamples().isEmpty())
            return;

        List<String> examples = new ArrayList<>();
        for (CommandUsageExample exampleRaw : command.getUsageExamples()) {
            examples.add(calculateExampleDisplay(fullName, exampleRaw));
        }

        String helpExamples = StringUtils.generateDisplayList("\n", "\n", examples);
        eb.addField("Examples", helpExamples, false);
    }

    /**
     * Calculates a displayable message for the example.
     *
     * @param fullName command's full name
     * @param example example to display
     * @return displayable message for the example
     */
    private static String calculateExampleDisplay(String fullName, CommandUsageExample example) {
        StringBuilder sb = new StringBuilder().append("`").append(GlobalConfiguration.Command.commandPrefix).append(fullName);

        if (!example.args.isEmpty()) {
            sb.append(" ")
                    .append(example.args);
        }
        sb.append("` - ").append(example.result);

        return sb.toString();
    }
}