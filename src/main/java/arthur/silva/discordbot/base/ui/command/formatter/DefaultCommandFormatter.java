package arthur.silva.discordbot.base.ui.command.formatter;

import arthur.silva.discordbot.base.data.loaded.configuration.GlobalConfiguration;
import arthur.silva.discordbot.base.infrastructure.application.utils.StringUtils;
import arthur.silva.discordbot.base.ui.command.base.CommandUsageExample;
import arthur.silva.discordbot.base.ui.command.base.CommandWithSubcommands;
import arthur.silva.discordbot.base.ui.command.base.CommandWithoutSubcommands;
import arthur.silva.discordbot.base.ui.command.formatter.type.CommandHelpFormatter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultCommandFormatter implements CommandHelpFormatter {
    @Override
    public Message format(Map.Entry<String, arthur.silva.discordbot.base.ui.command.base.Command> commandEntry) {
        arthur.silva.discordbot.base.ui.command.base.Command command = commandEntry.getValue();
        String fullName = commandEntry.getKey();

        if (command instanceof CommandWithoutSubcommands)
            return formatSpecific((CommandWithoutSubcommands) command, fullName);
        if (command instanceof CommandWithSubcommands)
            return formatSpecific((CommandWithSubcommands) command, fullName);

        return new MessageBuilder("There is no formatter for this type of command.").build();
    }

    private Message formatSpecific(CommandWithoutSubcommands command, String fullName) {
        return new CommandHelpGenerator().generateMessage(command, fullName);
    }

    private Message formatSpecific(CommandWithSubcommands command, String fullName) {
        return new CommandWithSubcommandsHelpGenerator().generate(command, fullName);
    }

}

class CommandWithSubcommandsHelpGenerator {

    public Message generate(CommandWithSubcommands command, String fullName) {
        CommandHelpGenerator commandHelpGenerator = new CommandHelpGenerator();
        EmbedBuilder eb = commandHelpGenerator.generateEmbedBuilder(command, fullName);
        setDescription(eb, command, fullName);

        return new MessageBuilder().setEmbed(eb.build()).build();
    }

    private void setDescription(EmbedBuilder eb, CommandWithSubcommands command, String fullName) {
        StringBuilder sb = new StringBuilder();
        sb.append("**").append(command.getDescription()).append("**\n");
        for (arthur.silva.discordbot.base.ui.command.base.Command subcommand : command.getSubcommands()) {
            sb.append("\n• ").append(subcommand.getNames().get(0));
        }

        eb.setDescription(sb.toString());
    }
}

class CommandHelpGenerator {
    public Message generateMessage(arthur.silva.discordbot.base.ui.command.base.Command command, String fullName) {
        MessageEmbed embed = generateEmbedBuilder(command, fullName).build();
        return new MessageBuilder().setEmbed(embed).build();
    }

    public EmbedBuilder generateEmbedBuilder(arthur.silva.discordbot.base.ui.command.base.Command command, String fullName) {
        EmbedBuilder eb = new EmbedBuilder();
        generateEmbedBuilder(eb, command, fullName);
        return eb;
    }

    public void generateEmbedBuilder(EmbedBuilder eb, arthur.silva.discordbot.base.ui.command.base.Command command, String fullName) {
        setHeader(eb, command, fullName);
        setAliases(eb, command);
        setColor(eb);
        setExamples(eb, command, fullName);
    }

    private void setHeader(EmbedBuilder eb, arthur.silva.discordbot.base.ui.command.base.Command command, String fullName) {
        String header = GlobalConfiguration.Command.commandPrefix + fullName + " *" +
                command.getArguments() + "*";
        eb.addField(header, "**" + command.getDescription() + "**", false);
    }

    private void setColor(EmbedBuilder eb) {
        eb.setColor(GlobalConfiguration.Command.defaultEmbedColor);
    }

    private void setAliases(EmbedBuilder eb, arthur.silva.discordbot.base.ui.command.base.Command command) {
        List<String> aliases = command.getNames();
        if (aliases.size() < 2) {
            return;
        }

        aliases.remove(0);
        String helpAliases = StringUtils.generateDisplayList(", ", ", ", aliases);
        eb.addField("Aliases", helpAliases, false);
    }

    private void setExamples(EmbedBuilder eb, arthur.silva.discordbot.base.ui.command.base.Command command, String fullName) {
        if (command.getUsageExamples().isEmpty())
            return;

        List<String> examples = new ArrayList<>();
        for (CommandUsageExample example : command.getUsageExamples()) {
            examples.add(calculateExampleDisplay(fullName, example));
        }

        String helpExamples = StringUtils.generateDisplayList("\n", "\n", examples);
        eb.addField("Examples", helpExamples, false);
    }

    private String calculateExampleDisplay(String fullName, CommandUsageExample example) {
        StringBuilder sb = new StringBuilder().append("`").append(fullName).append(" ")
                .append(example.args).append("` - ")
                .append(example.result);

        if (!example.args.isEmpty()) {
            sb.append(fullName).append(" ")
                    .append(example.args);
        }
        sb.append("` - ").append(example.result);

        return sb.toString();
    }
}