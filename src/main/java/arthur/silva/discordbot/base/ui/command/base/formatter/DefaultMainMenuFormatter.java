package arthur.silva.discordbot.base.ui.command.base.formatter;

import arthur.silva.discordbot.base.data.loaded.configuration.GlobalConfiguration;
import arthur.silva.discordbot.base.infrastructure.application.utils.EmbedUtils;
import arthur.silva.discordbot.base.infrastructure.application.utils.StringUtils;
import arthur.silva.discordbot.base.ui.command.base.Category;
import arthur.silva.discordbot.base.ui.command.base.Command;
import arthur.silva.discordbot.base.ui.command.base.CommandsManager;
import arthur.silva.discordbot.base.ui.command.base.formatter.type.MainMenuFormatter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.*;

/**
 * Formats commands' main menus.
 */
public class DefaultMainMenuFormatter implements MainMenuFormatter {

    @Override
    public Message format(Collection<Command> commands) {
        return generateHelpMessage(commands);
    }

    private Message generateHelpMessage(Collection<Command> commandsToList) {
        MessageEmbed messageContent = generateHelpEmbed(commandsToList);
        return new MessageBuilder().setEmbed(messageContent).build();
    }

    private MessageEmbed generateHelpEmbed(Collection<Command> commandsToList) {
        Map<Category, String> categoriesHelp = generateCategoriesDescriptions(commandsToList);

        EmbedBuilder helpEb = new EmbedBuilder();
        for (Map.Entry<Category, String> catHelpEntry : categoriesHelp.entrySet()) {
            helpEb.addField(catHelpEntry.getKey().getName(), catHelpEntry.getValue(), true);
        }
        EmbedUtils.adjustEmbedInlineFields(helpEb, 2);

        helpEb.setColor(GlobalConfiguration.Command.defaultEmbedColor);
        GlobalConfiguration.Command.configEmbedThumbnail(helpEb);
        GlobalConfiguration.Command.configHelpEmbedFooter(helpEb);

        return helpEb.build();
    }

    /**
     * Calculates the description message for each category.
     *
     * @param commands the commands to calculate the descriptions for
     * @return the description per category
     */
    private Map<Category, String> generateCategoriesDescriptions(Collection<Command> commands) {
        Map<Category, Set<Command>> commandsByCategory = CommandsManager.Utils.splitCommandsByCategory(commands);

        Map<Category, String> categoriesHelp = new LinkedHashMap<>();
        for (Map.Entry<Category, Set<Command>> commandCategoryEntry : commandsByCategory.entrySet()) {
            Set<Command> categoryCommands = commandCategoryEntry.getValue();

            String catHelp = getCategoryHelpMessage(categoryCommands);
            categoriesHelp.put(commandCategoryEntry.getKey(), catHelp);
        }
        return categoriesHelp;
    }

    /**
     * Calculates the display format for a list of commands.
     *
     * @param commands the commands to format
     * @return the formatted message
     */
    private String getCategoryHelpMessage(Collection<Command> commands) {
        StringBuilder sb = new StringBuilder();
        Iterator<Command> it = commands.iterator();
        if (it.hasNext())
            sb.append(" • ").append(it.next().getDefaultName());
        it.forEachRemaining(cmd -> {
            sb.append("\n • ").append(cmd.getDefaultName());
        });
        sb.append("\n").append(StringUtils.getInvisibleCharacter());
        return sb.toString();
    }
}
