package discord.bot.ui.command.base;

/**
 * Example of a command usage.
 */
public class CommandUsageExample {
    public final String args;
    public final String result;

    /**
     * @param args arguments passed
     * @param result result with the given arguments
     */
    public CommandUsageExample(String args, String result) {
        this.args = args;
        this.result = result;
    }
}
