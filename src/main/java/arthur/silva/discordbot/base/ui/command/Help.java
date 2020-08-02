package arthur.silva.discordbot.base.ui.command;

import arthur.silva.discordbot.base.application.command.CommandExecutor;
import arthur.silva.discordbot.base.application.events.MessageReceivedEvent;
import arthur.silva.discordbot.base.ui.command.base.Category;
import arthur.silva.discordbot.base.ui.command.base.Command;
import arthur.silva.discordbot.base.ui.command.base.CommandWithoutSubcommands;
import net.dv8tion.jda.api.entities.Message;
import org.springframework.stereotype.Component;

@Component
public class Help extends CommandWithoutSubcommands {

    public Help() {
        super(Category.INFORMATION, "Obtain help related to the bot.", false);
        // TODO
    }

    /**
     * Execute this particular command's actions.
     *
     * @param messageReceivedEvent the event that triggered the command
     * @param nCommandsInChain     the number of commands so far down the chain
     */
    @Override
    protected void runInternal(MessageReceivedEvent messageReceivedEvent, int nCommandsInChain) {
        // TODO
    }

    public Message getHelpFor(Command command) {
        // TODO
        throw new UnsupportedOperationException("arthur.silva.discordbot.base.ui.command.Help TODO");
    }

    public void buildMenus(CommandExecutor commandExecutor) {
        // TODO
    }
}
