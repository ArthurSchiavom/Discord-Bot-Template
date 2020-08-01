package arthur.silva.discordbot.base.ui.command.base;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * A command without sub-commands
 */
public abstract class CommandWithoutSubCommands extends Command {
    private String arguments;
    private List<String> examples = new ArrayList<>();

    /**
     * Creates a new command without subcommands.
     *
     * @param category The command's category.
     * @param description The command's description.
     * @param arguments This commands arguments as "[param1] [param2] ...". Can be null.
     * @param runInNewThread If the command actions should run on a new thread.
     * @param superCommand This command's super command.
     */
    public CommandWithoutSubCommands(Category category, String description, String arguments, boolean runInNewThread, Command superCommand) {
        super(category, description, runInNewThread, superCommand);
        initialize(arguments);
    }

    /**
     * Creates a new command without subcommands.
     *
     * @param category The command's category.
     * @param description The command's description.
     * @param arguments This commands arguments as "[param1] [param2] ...". Can be null.
     * @param runInNewThread If the command actions should run on a new thread.
     */
    public CommandWithoutSubCommands(Category category, String description, String arguments, boolean runInNewThread) {
        super(category, description, runInNewThread);
        initialize(arguments);
    }

    /**
     * Initializes this class.
     *
     * @param arguments This command's arguments.
     */
    private void initialize(String arguments) {
        if (arguments == null)
            this.arguments = "";
        else
            this.arguments = arguments;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void buildHelpMessage() {
        /*
        EMBEDDED

        *full name [arg1] [arg2]
        description

        *examples
        example1
        example2

        *aliases
        alias1, alias2

        Footer: Random fact about the bot or something else
         */
        MessageBuilder messageBuilder = new MessageBuilder();
        EmbedBuilder eb = new EmbedBuilder();

        setHelpEmbedHeader(eb);
        this.setHelpEmbedExamples(eb, examples);
        this.setHelpEmbedAliases(eb, this.getNames());
        this.setHelpEmbedColor(eb);

        messageBuilder.setEmbed(eb.build());
        this.setHelpMessage(messageBuilder.build());
    }

    /**
     * Appends the help embed header to an embed.
     *
     * @param eb The target embed.
     */
    private void setHelpEmbedHeader(EmbedBuilder eb) {
        StringBuilder header = new StringBuilder();
        header.append(this.calcFullNameWithPrefix()).append(" *").append(arguments)
                .append("*");
        eb.addField(header.toString(), "**" + this.getDescription() + "**", false);
    }

    /**
     * Adds an example.
     *
     * @param args The arguments for the command.
     * @param description The description for these arguments.
     */
    protected final void addExample(String args, String description) {
        examples.add("`" + this.calcFullNameWithPrefix() + " " + args + "` - " + description);
    }
}
