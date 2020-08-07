package arthur.silva.discordbot.base.ui.command;

import arthur.silva.discordbot.base.application.events.MessageReceivedEvent;
import arthur.silva.discordbot.base.data.loaded.configuration.GlobalConfiguration;
import arthur.silva.discordbot.base.ui.command.base.*;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Command to obtain help/info on using the bot.
 */
@Component
public class Help extends CommandWithoutSubcommands {

    public static final String DEFAULT_NAME = "Help";
    private final String ADMIN_MENU_NAME_LOWERCASE = "admin";

    @Autowired
    private CommandsHelpManager commandsHelpManager;

    public Help() {
        super(Category.INFORMATION, "What can I do? Use this command to find out!", false);
        this.registerName(DEFAULT_NAME);
        this.registerName("H");
    }

    @Override
    protected void runInternal(MessageReceivedEvent event) {
        String commandToFindHelpFor = event.getMessage().getContentDisplay();
        commandToFindHelpFor = Command.removePrefixAndNArguments(commandToFindHelpFor, this.getDepth())
                .toLowerCase();

        Message reply;
        switch (commandToFindHelpFor) {
            case ADMIN_MENU_NAME_LOWERCASE:
                reply = commandsHelpManager.getMainMenu(MainMenuType.ADMIN);
                break;
            case "":
                reply = commandsHelpManager.getMainMenu(MainMenuType.USER);
                break;
            default:
                reply = commandsHelpManager.getHelpMessageFor(commandToFindHelpFor, false);
        }

        if (reply == null) {
            reply = new MessageBuilder("Sorry! The command `" + commandToFindHelpFor + "` doesn't exist." +
                    " You can use `" + GlobalConfiguration.Command.commandPrefix + Help.DEFAULT_NAME + "` to check all the commands.").build();
        }

        event.getChannel().sendMessage(reply).queue();
    }
}
