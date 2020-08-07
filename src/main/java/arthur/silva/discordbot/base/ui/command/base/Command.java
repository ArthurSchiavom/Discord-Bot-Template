package arthur.silva.discordbot.base.ui.command.base;

import arthur.silva.discordbot.base.application.events.MessageReceivedEvent;
import arthur.silva.discordbot.base.data.loaded.configuration.GlobalConfiguration;
import arthur.silva.discordbot.base.ui.command.base.requirement.Requirement;
import arthur.silva.discordbot.base.ui.command.base.requirement.RequirementVerificationResult;
import arthur.silva.discordbot.base.ui.command.base.requirement.RequirementsManager;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A Discord command.
 */
public abstract class Command {

    @Getter private final Category category;
    @Getter private final String description;
    private final List<String> names; // TODO - The CommandExecutor will trace-back the names of all parents to create the menu.
    private final List<String> arguments;
    private final List<CommandUsageExample> usageExamples;
    private final boolean runInNewThread;
    @Autowired @Getter private RequirementsManager requirementsManager;
    private int depth = 1;

    /**
     * @return this command's names
     */
    public List<String> getNames() {
        return new ArrayList<>(names);
    }

    /**
     * @return this command's default name
     */
    public String getDefaultName() {
        return names.get(0);
    }

    /**
     * @return the arguments required by this command
     */
    public List<String> getArguments() {
        return new ArrayList<>(arguments);
    }

    /**
     * @return examples of how to use this command
     */
    public List<CommandUsageExample> getUsageExamples() {
        return new ArrayList<>(usageExamples);
    }

    /**
     * Increments this commands depth, meaning how many commands there is in the chain.
     * <br>Example 1: a command with no supercommands has depth 1
     * <br>Example 2: a command with a supercommand has depth 2
     * <br>Example 3: a command with a supercommand that has a supercommand has depth 3
     */
    protected void incrementDepth() {
        depth++;
    }

    /**
     * Retrieves this command's depth in the command chain. In other words, how many supercommands it has +1;
     *
     * @return command's depth
     */
    protected int getDepth() {
        return depth;
    }

    /**
     * Creates a new command.
     *
     * @param category The command's category.
     * @param description The command's description.
     * @param runInNewThread If the command actions should run on a new thread.
     */
    public Command(@NonNull Category category, @NonNull String description, boolean runInNewThread, @NonNull String... arguments) {
        if (description.isEmpty()) {
            throw new IllegalArgumentException("A description must be specified.");
        }
        this.category = category;
        this.description = description;
        this.runInNewThread = runInNewThread;
        names = new ArrayList<>();
        usageExamples = new ArrayList<>();
        this.arguments = Arrays.asList(arguments);
    }

    /**
     * Registers a new name for this command. <b>The first name to be registered is the one displayed in the menus.</b>
     *
     * @param name the name to register
     */
    protected void registerName(@NonNull String name) {
        if (name.contains(GlobalConfiguration.Command.ARGUMENT_SEPARATOR)) {
            throw new IllegalArgumentException("Command names can't include the reserved character '" + GlobalConfiguration.Command.ARGUMENT_SEPARATOR + "'.");
        }
        names.add(name);
    }

    /**
     * Registers an usage example.
     *
     * @param usageExample usage example
     */
    protected void registerUsageExample(@NonNull CommandUsageExample usageExample) {
        usageExamples.add(usageExample);
    }

    /**
     * Registers requirements to run this command.
     *
     * @param first requirement to register
     * @param others other requirements to register
     */
    protected void registerRequirements(Requirement first, @NonNull Requirement... others) {
        requirementsManager.setRequirements(first, others);
    }

    /**
     * Executes the command.
     *
     * @param event the event that triggered the command
     */
    public void run(@NonNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;

        RequirementVerificationResult requirementVerificationResult = requirementsManager.meetsRequirements(event);
        if (!requirementVerificationResult.successfulVerification) {
            event.getChannel().sendMessage(requirementVerificationResult.reasonVerificationFailed).queue();
            return;
        }

        if (runInNewThread) {
            new Thread(() -> runInternal(event)).start();
        }
        else {
            runInternal(event);
        }
    }

    /**
     * Execute this particular command's actions.
     *
     * @param event the event that triggered the command
     */
    protected abstract void runInternal(MessageReceivedEvent event);

    /**
     * Removes the command prefix from the message and N arguments.
     *
     * @param msg the message to process
     * @param nArguments the number of arguments to remove
     * @return (1) the processed message or (2) an empty string if the message doesn't contain enough arguments or is smaller than the prefix
     */
    public static String removePrefixAndNArguments(@NonNull String msg, int nArguments) {
        msg = removePrefix(msg);
        return removeNArguments(msg, nArguments);
    }

    /**
     * Removes N arguments from the command.
     *
     * @param msg the message to process
     * @param nArguments the number of arguments to remove
     * @return (1) the processed message or (2) an empty string if the message has less arguments than the amount specified.
     */
    public static String removeNArguments(@NonNull String msg, int nArguments) {
        String[] split = msg.split(GlobalConfiguration.Command.ARGUMENT_SEPARATOR, nArguments+1);
        if (split.length <= nArguments)
            return "";
        else
            return split[nArguments];
    }

    /**
     * Removes the prefix from a message.
     * If the message doesn't contain the prefix, still removes the amount of characters from the beginning of the text.
     *
     * @param msg the message to unregister the prefix from
     * @return (1) the message without the prefix or (2) an empty string if the message is smaller than the prefix length
     */
    public static String removePrefix(@NonNull String msg) {
        if (msg.length() < GlobalConfiguration.Command.commandPrefixNChars)
            return "";

        return msg.substring(GlobalConfiguration.Command.commandPrefixNChars);
    }
}
