package discord.bot.ui.command.base;

import discord.bot.ui.command.base.requirement.Requirement;

/**
 * A commands' main menu type.
 */
public enum MainMenuType {
    /**
     *
     * !!! Be sure to verify all elements when updating/adding/removing elements/permissions. Some permissions can affect other menus.
     *
     * */
    USER("User",
            command -> command.getRequirementsManager().doesNotRequire(Requirement.ADMIN))

    , ADMIN("Admin",
                command -> command.getRequirementsManager().requires(Requirement.ADMIN))
    ;

    public final String NAME;
    private final AcceptCommandAction ACCEPT_COMMAND_ACTION;

    MainMenuType(String name, AcceptCommandAction acceptCommandAction) {
        this.NAME = name;
        this.ACCEPT_COMMAND_ACTION = acceptCommandAction;
    }

    /**
     * Verifies if this main menu accepts a given command.
     *
     * @param command the command to verify
     * @return true if the command is accepted and false otherwise
     */
    public boolean accepts(Command command) {
        return ACCEPT_COMMAND_ACTION.accepts(command);
    }

    /**
     * Callback to verify if a menu accepts a command.
     */
    private interface AcceptCommandAction {
        boolean accepts(Command command);
    }
}
