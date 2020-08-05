package arthur.silva.discordbot.base.ui.command;

import arthur.silva.discordbot.base.data.loaded.configuration.GlobalConfiguration;
import arthur.silva.discordbot.base.ui.command.base.*;
import arthur.silva.discordbot.base.application.events.MessageReceivedEvent;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

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

    /**
     * Execute this particular command's actions.
     *
     * @param event the event that triggered the command
     */
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
