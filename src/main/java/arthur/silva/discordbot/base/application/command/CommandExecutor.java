package arthur.silva.discordbot.base.application.command;


import arthur.silva.discordbot.base.application.command.requirement.Requirement;
import arthur.silva.discordbot.base.data.loaded.configuration.CommandUserConfig;
import arthur.silva.discordbot.base.ui.command.base.Category;
import arthur.silva.discordbot.base.ui.command.base.Command;
import arthur.silva.discordbot.base.application.events.MessageReceivedEvent;
import arthur.silva.discordbot.base.application.utils.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * The command executor registers and executes commands. <b>Register the commands on the initialization method</b>.
 */
@Component
public class CommandExecutor {
	private static final String MAIN_MENU_HELP_THUMBNAIL = "https://live.staticflickr.com/65535/48673315806_a35665d72f_o_d.png";

	private Message ownerHelp;
	private Message adminUserHelp;
	private Message regularUserHelp;
	private Message adminUserHelpMainGuild;
	private Message regularUserHelpMainGuild;

	private List<Command> commands = new ArrayList<>();
	private List<String> commandsNames = new ArrayList<>();

	private CommandExecutor() {
//		Help helpCommand = new Help();
//
//		registerCommand(helpCommand);
	}

	/**
	 * @return The regular user help message.
	 */
	public Message getRegularUserHelp() {
		return regularUserHelp;
	}

	/**
	 * @return The main guild regular user help message.
	 */
	public Message getRegularUserHelpMainGuild() {
		return regularUserHelpMainGuild;
	}

	/**
	 * @return The main guild admin help message.
	 */
	public Message getAdminUserHelpMainGuild() {
		return adminUserHelpMainGuild;
	}

	/**
	 * @return The admin user help message.
	 */
	public Message getAdminUserHelp() {
		return adminUserHelp;
	}

	/**
	 * @return The owner help message.
	 */
	public Message getOwnerHelp() {
		return ownerHelp;
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
	public void registerCommand(Command cmd) {
		commands.add(cmd);
		commandsNames.addAll(cmd.getNames());
	}

	/**
	 * Verifies if the given name is the name of a command already registered.
	 *
	 * @param name The name to verify.
	 * @return If the given name is the name of a command already registered.
	 */
	public boolean isCommandNameRegistered(String name) {
		name.toLowerCase();
		return commandsNames.contains(name);
	}

	/**
	 * Executes a command request, if it is a proper command request.
	 *
	 * @param msgEvent the message that might be a command request.
	 */
	public void executePossibleCommandRequest(MessageReceivedEvent msgEvent) {
		Command commandToRun = getCommand(msgEvent);
		if (commandToRun != null)
			commandToRun.run(msgEvent);
	}

	/**
	 * Finds the command that the message is requesting, if it is.
	 *
	 * @param msgEvent The event containing the message to analyse.
	 * @return The command request or
	 * <br>null if no command is requested in the event.
	 */
	public Command getCommand(MessageReceivedEvent msgEvent) {
		String message;
		try {
			message = msgEvent.getMessage().getContentDisplay().toLowerCase();
			if (!message.startsWith(CommandUserConfig.commandPrefix))
				return null;
		} catch (StringIndexOutOfBoundsException e) {
			/* This means that the message wasn't long enough to contain the prefix */
			return null;
		}

		if (message != null) {
			String command = message.substring(CommandUserConfig.commandPrefixNChars);
			for (Command commandRegistered : commands) {
				Command commandToRun = commandRegistered.isThisCommand(command);
				if (commandToRun != null)
					return commandToRun;
			}
		}
		return null;
	}

	/**
	 * Updates all help messages.
	 */
	public void updateAllHelp() {
		updateRegularHelp();
		updateAdminHelp();
		updateOwnerHelp();
	}

	/**
	 * Updates the regular user help message.
	 */
	public void updateRegularHelp() {
		// TODO
//		regularUserHelp = new MessageBuilder(calculateGenericHelp(null, null)).build();
//
//		Requirement[] mayAlsoHaveRequirementsMainGuild = {Requirement.MAIN_GUILD};
//		regularUserHelpMainGuild = new MessageBuilder(calculateGenericHelp(null, mayAlsoHaveRequirementsMainGuild)).build();
	}

	/**
	 * Updates the regular user help message.
	 */
	public void updateAdminHelp() {
		// TODO
//		Requirement[] requirements = {Requirement.ADMIN};
//		adminUserHelp = new MessageBuilder(calculateGenericHelp(requirements, null)).build();
//
//		Requirement[] requirementsMainGuild = {Requirement.ADMIN};
//		Requirement[] mayAlsoHaveRequirementsMainGuild = {Requirement.MAIN_GUILD};
//		adminUserHelpMainGuild = new MessageBuilder(calculateGenericHelp(requirementsMainGuild, mayAlsoHaveRequirementsMainGuild)).build();
	}

	/**
	 * Updates the owner help message.
	 */
	public void updateOwnerHelp() {
		// TODO
//		Requirement[] mustHaveRequirements = {Requirement.OWNER};
//		Requirement[] mayAlsoHaveRequirements = {Requirement.MAIN_GUILD};
//		ownerHelp = new MessageBuilder(calculateGenericHelp(mustHaveRequirements, mayAlsoHaveRequirements)).build();
	}

	/**
	 * Calculates a generic command list help.
	 *
	 * @param mustHaveRequirements The commands must have these requirements. Can be null
	 * @param mayAlsoHaveRequirements The commands may also have these requirements. Can be null
	 */
	private EmbedBuilder calculateGenericHelp(Requirement[] mustHaveRequirements, Requirement[] mayAlsoHaveRequirements) {
		List<Command> commandsToList = getCommands(mustHaveRequirements, mayAlsoHaveRequirements);
		TreeMap<Category, String> categoriesHelp = calcCategoriesHelp(commandsToList);

		EmbedBuilder helpEb = new EmbedBuilder();
		for (Category cat : categoriesHelp.keySet()) {
			helpEb.addField(cat.getName(), categoriesHelp.get(cat), true);
		}
		Utils.adjustEmbedInlineFields(helpEb, 2);

		helpEb.setColor(CommandUserConfig.defaultEmbedColor);
		helpEb.setThumbnail(MAIN_MENU_HELP_THUMBNAIL);
		CommandUserConfig.configHelpEmbedFooter(helpEb);
		return helpEb;
	}

	private TreeMap<Category, String> calcCategoriesHelp(List<Command> commands) {
		Map<Category, List<Command>> commandsByCategory = getCommandsByCategory(commands);

		TreeMap<Category, String> categoriesHelp = new TreeMap<>();
		for (Category cat : commandsByCategory.keySet()) {
			List<Command> categoryCommands = commandsByCategory.get(cat);

			if (!categoryCommands.isEmpty()) {
				String catHelp = getCategoryHelpMessage(categoryCommands);
				categoriesHelp.put(cat, catHelp);
			}
		}
		return categoriesHelp;
	}

	/**
	 * Calculates the help message for a category.
	 *
	 * @param commands The category commands.
	 * @return The help message for a category.
	 */
	private String getCategoryHelpMessage(List<Command> commands) {
		StringBuilder sb = new StringBuilder();
		Iterator<Command> it = commands.iterator();
		if (it.hasNext())
			sb.append(" • ").append(it.next().getName());
		it.forEachRemaining(cmd -> {
			sb.append("\n").append(" • ").append(cmd.getName());
		});
		sb.append("\n" + Utils.getInvisibleCharacter());
		return sb.toString();
	}

	/**
	 * Separates a list of commands by category.
	 *
	 * @param allCommands The list of commands to separate.
	 * @return A map of commands by category.
	 */
	public Map<Category, List<Command>> getCommandsByCategory(List<Command> allCommands) {
		Map<Category, List<Command>> result = new HashMap<>();
		for (Category cat : Category.values()) {
			result.put(cat, new ArrayList<Command>());
		}

		for (Command cmd : allCommands) {
			result.get(cmd.getCategory()).add(cmd);
		}
		return result;
	}

	public List<Command> getCommands(Requirement[] requirements, Requirement[] mayAlsoHaveRequirements) {
		List<Command> result = new ArrayList<>();

		if (requirements == null)
			requirements = new Requirement[0];

		for (Command command : commands) {
			if (command.getRequirementsManager().requiresOnly(requirements, mayAlsoHaveRequirements))
				result.add(command);
		}

		return result;
	}

	/**
	 * Unregisters a command.
	 * <br><b>Note that this doesn't unregister the command from the user help menu</b>
	 *
	 * @param command The command to unregister.
	 */
	public void removeCommand(Command command) {
		commands.remove(command);
	}
}
