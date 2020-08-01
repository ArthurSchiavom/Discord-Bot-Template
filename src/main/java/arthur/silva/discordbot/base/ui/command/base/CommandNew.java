package arthur.silva.discordbot.base.ui.command.base;

import arthur.silva.discordbot.base.application.command.requirement.Requirement;
import arthur.silva.discordbot.base.application.command.requirement.RequirementVerificationResult;
import arthur.silva.discordbot.base.application.command.requirement.RequirementsManager;
import arthur.silva.discordbot.base.application.events.MessageReceivedEvent;
import arthur.silva.discordbot.base.data.loaded.configuration.CommandUserConfig;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public abstract class CommandNew {

    private static final String RESERVED_CHARACTER_FOR_NAME = " "; // Used to separate parent/child commands

    @Getter private final Category category;
    @Getter private final String description;
    @Getter private final CommandNew parentCommand;
    private final List<String> names; // TODO - The CommandExecutor will trace-back the names of all parents to create the menu.
    private final List<CommandUsageExample> usageExamples;
    private final boolean runInNewThread;
    @Getter private final short nParents;
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
    public CommandNew(@NonNull Category category, @NonNull String description, boolean runInNewThread) {
        this(category, description, runInNewThread, null);
    }

    /**
     * Creates a new command.
     *
     * @param category The command's category.
     * @param description The command's description.
     * @param runInNewThread If the command actions should run on a new thread.
     * @param parentCommand This command's super command.
     */
    public CommandNew(@NonNull Category category, @NonNull String description, boolean runInNewThread, @Nullable CommandNew parentCommand) {
        if (description.isEmpty()) {
            throw new IllegalArgumentException("A description must be specified.");
        }
        this.category = category;
        this.description = description;
        this.runInNewThread = runInNewThread;
        this.parentCommand = parentCommand;
        names = new ArrayList<>();
        usageExamples = new ArrayList<>();
        nParents = calculateNumberOfParents();
    }

    /**
     * Registers a new name for this command. <b>The first name to be registered is the one displayed in the menus.</b>
     *
     * @param name the name to register
     */
    protected void registerName(@NonNull String name) {
        if (name.contains(RESERVED_CHARACTER_FOR_NAME)) {
            throw new IllegalArgumentException("Command names can't include the reserved character '" + RESERVED_CHARACTER_FOR_NAME + "'.");
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
     * Removes the prefix and this command's name from the message.
     * Internally, it just removes the prefix and the first arguments according to the number of parent commands.
     *
     * @param msg the target message
     * @return (1) the message with the prefix and command names removed or (2) null if there aren't enough arguments to remove
     */
    protected String retrieveArgumentsOnly(@NonNull String msg) {
        return removePrefixAndCommandName(msg);
    }

    /**
     * Removes the prefix and this command's name from the message.
     * Internally, it just removes the prefix and the first arguments according to the number of parent commands.
     *
     * @param msg the target message
     * @return (1) the message with the prefix and command names removed or (2) an empty string if there aren't enough arguments to remove
     */
    protected String removePrefixAndCommandName(@NonNull String msg) {
        msg = removePrefix(msg);
        String[] split = msg.split(" ", nParents + 2); // + este comando + o resultado
        if (split.length <= nParents + 2)
            return "";
        else
            return split[nParents + 1];
    }

    /**
     * Removes the prefix from a message.
     * If the message doesn't contain the prefix, still removes the amount of characters from the beginning of the text.
     *
     * @param msg the message to unregister the prefix from
     * @return (1) the message without the prefix or (2) if the message is smaller than the prefix length
     */
    protected String removePrefix(@NonNull String msg) {
        if (msg.length() < CommandUserConfig.commandPrefixNChars)
            return "";

        return msg.substring(CommandUserConfig.commandPrefixNChars);
    }

    /**
     * Calculates the number of parent commands.
     *
     * @return the number of parent commands
     */
    private short calculateNumberOfParents() {
        if (parentCommand == null) {
            return 0;
        }

        short nParents = 1;
        CommandNew currentParent = parentCommand;
        while (currentParent.parentCommand != null) {
            nParents++;
            currentParent = currentParent.parentCommand;
        }
        return nParents;
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
        }

        if (runInNewThread) {
            new Thread(() -> {
                runInternal(event);
            }).start();
        }
        else {
            runInternal(event);
        }
    }

    /**
     * Execute this particular command's actions.
     *
     * @param messageReceivedEvent the event that triggered the command
     */
    protected abstract void runInternal(MessageReceivedEvent messageReceivedEvent);
}
