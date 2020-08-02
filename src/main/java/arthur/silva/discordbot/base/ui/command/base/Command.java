package arthur.silva.discordbot.base.ui.command.base;

import arthur.silva.discordbot.base.application.command.requirement.Requirement;
import arthur.silva.discordbot.base.application.command.requirement.RequirementVerificationResult;
import arthur.silva.discordbot.base.application.command.requirement.RequirementsManager;
import arthur.silva.discordbot.base.application.events.MessageReceivedEvent;
import arthur.silva.discordbot.base.data.loaded.configuration.CommandUserConfig;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public abstract class Command {

    public static final String ARGUMENT_SEPARATOR = " "; // Used to separate parent/child commands

    @Getter private final Category category;
    @Getter private final String description;
    private final List<String> names; // TODO - The CommandExecutor will trace-back the names of all parents to create the menu.
    private final List<CommandUsageExample> usageExamples;
    private final boolean runInNewThread;
    @Autowired @Getter private RequirementsManager requirementsManager;

    public List<String> getNames() {
        return new ArrayList<>(names);
    }

    public List<CommandUsageExample> getUsageExamples() {
        return new ArrayList<>(usageExamples);
    }

    /**
     * Creates a new command.
     *
     * @param category The command's category.
     * @param description The command's description.
     * @param runInNewThread If the command actions should run on a new thread.
     */
    public Command(@NonNull Category category, @NonNull String description, boolean runInNewThread) {
        if (description.isEmpty()) {
            throw new IllegalArgumentException("A description must be specified.");
        }
        this.category = category;
        this.description = description;
        this.runInNewThread = runInNewThread;
        names = new ArrayList<>();
        usageExamples = new ArrayList<>();
    }

    /**
     * Registers a new name for this command. <b>The first name to be registered is the one displayed in the menus.</b>
     *
     * @param name the name to register
     */
    protected void registerName(@NonNull String name) {
        if (name.contains(ARGUMENT_SEPARATOR)) {
            throw new IllegalArgumentException("Command names can't include the reserved character '" + ARGUMENT_SEPARATOR + "'.");
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
     * @param requirements requirements to register
     */
    protected void registerRequirements(@NonNull Requirement... requirements) {
        requirementsManager.registerRequirements(requirements);
    }

    /**
     * Executes the command. It is assumed that this command doesn't have a subcommand.
     *
     * @param event the event that triggered the command
     */
    public void run(@NonNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;

        RequirementVerificationResult requirementVerificationResult = requirementsManager.meetsRequirements(event);
        if (!requirementVerificationResult.successfulVerification) {
            event.getChannel().sendMessage(requirementVerificationResult.reasonVerificationFailed).queue();
        }

        final int nCommandsInChain = 1;

        if (runInNewThread) {
            new Thread(() -> {
                runInternal(event, nCommandsInChain);
            }).start();
        }
        else {
            runInternal(event, nCommandsInChain);
        }
    }

    /**
     * Executes the command.
     *
     * @param event the event that triggered the command
     * @param nCommandsInChain the number of commands so far down the chain
     */
    protected void run(@NonNull MessageReceivedEvent event, int nCommandsInChain) {
        if (event.getAuthor().isBot())
            return;

        RequirementVerificationResult requirementVerificationResult = requirementsManager.meetsRequirements(event);
        if (!requirementVerificationResult.successfulVerification) {
            event.getChannel().sendMessage(requirementVerificationResult.reasonVerificationFailed).queue();
        }

        nCommandsInChain++;

        if (runInNewThread) {
            final int finalNCommands = nCommandsInChain;
            new Thread(() -> {
                runInternal(event, finalNCommands);
            }).start();
        }
        else {
            runInternal(event, nCommandsInChain);
        }
    }

    /**
     * Execute this particular command's actions.
     *
     * @param messageReceivedEvent the event that triggered the command
     * @param nCommandsInChain the number of commands so far down the chain
     */
    protected abstract void runInternal(MessageReceivedEvent messageReceivedEvent, int nCommandsInChain);

    /**
     * Removes the command prefix from the message and N arguments.
     *
     * @param msg the message to process
     * @param nArguments the number of arguments to remove
     * @return the processed message
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
        String[] split = msg.split(ARGUMENT_SEPARATOR, nArguments+1);
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
        if (msg.length() < CommandUserConfig.commandPrefixNChars)
            return "";

        return msg.substring(CommandUserConfig.commandPrefixNChars);
    }


}
