package arthur.silva.discordbot.base.ui.command.base;

import arthur.silva.discordbot.base.application.events.MessageReceivedEvent;
import arthur.silva.discordbot.base.ui.command.base.requirement.Requirement;

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

    public boolean accepts(Command command) {
        return ACCEPT_COMMAND_ACTION.accepts(command);
    }

    private interface AcceptCommandAction {
        boolean accepts(Command command);
    }
}
