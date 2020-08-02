package arthur.silva.discordbot.base.application.command;


import arthur.silva.discordbot.base.application.command.requirement.Requirement;
import arthur.silva.discordbot.base.data.loaded.configuration.CommandUserConfig;
import arthur.silva.discordbot.base.ui.command.Help;
import arthur.silva.discordbot.base.ui.command.base.Category;
import arthur.silva.discordbot.base.ui.command.base.Command;
import arthur.silva.discordbot.base.application.events.MessageReceivedEvent;
import arthur.silva.discordbot.base.application.utils.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * The command executor registers and executes commands. <b>Register the commands on the initialization method</b>.
 */
@Component
public class CommandExecutor {
	private List<Command> commands = new ArrayList<>();

	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private Help helpCommand;

	@PostConstruct
	private void setup() {
		commands = applicationContext.getBean("commandsInOrder", List.class);
		helpCommand.buildMenus(this);
	}

	/**
	 * @return The commands registered.
	 */
	public List<Command> getCommandsRegistered() {
		return new ArrayList<>(commands);
	}

	/**
	 * Registers a command.
	 * <br><b>Only commands WITHOUT a super-command should be registered.</b>
	 *
	 * @param cmd The command to register.
	 */
	public void registerCommand(@NonNull Command cmd) {
		commands.add(cmd);
	}

	/**
	 * Executes a command request, if it is a proper command request.
	 *
	 * @param event the message that might be a command request
	 *
	 * @return if any command was executed
	 */
	public boolean executePossibleCommandRequest(@NonNull MessageReceivedEvent event) {
		String possibleCommandName = Command
				.removePrefix(event.getMessage().getContentDisplay())
				.split(Command.ARGUMENT_SEPARATOR)[0];

		Command commandToRun = retrieveCommand(possibleCommandName);
		if (commandToRun == null) {
			return false;
		}
		commandToRun.run(event);
		return true;
	}

	/**
	 * Retrieves a command by it's name.
	 *
	 * @param nameToFind the name of the command to find
	 * @return (1) the command if such is found or (2) null if no command with the given name is found
	 */
	@Nullable
	public Command retrieveCommand(@NonNull String nameToFind) {
		for (Command command : commands) {
			for (String name : command.getNames()) {
				if (name.equalsIgnoreCase(nameToFind)) {
					return command;
				}
			}
		}

		return null;
	}

	/**
	 * Separates a list of commands by category.
	 *
	 * @param allCommands the list of commands to separate
	 * @return a map of commands by category
	 */
	public Map<Category, List<Command>> retrieveCommandsByCategory(List<Command> allCommands) {
		Map<Category, List<Command>> result = new HashMap<>();
		for (Category cat : Category.values()) {
			result.put(cat, new ArrayList<Command>());
		}

		for (Command cmd : allCommands) {
			result.get(cmd.getCategory()).add(cmd);
		}
		return result;
	}

	/**
	 * Retrieves the commands that meet the given requirements criteria.
	 *
	 * @param requirements the must have requirements
	 * @param mayAlsoHaveRequirements the optional requirements
	 *
	 * @return the commands filtered
	 */
	public List<Command> retrieveCommands(Requirement[] requirements, Requirement[] mayAlsoHaveRequirements) {
		List<Command> result = new ArrayList<>();

		if (requirements == null)
			requirements = new Requirement[0];

		for (Command command : commands) {
			if (command.getRequirementsManager().requiresOnly(requirements, mayAlsoHaveRequirements))
				result.add(command);
		}

		return result;
	}
}
