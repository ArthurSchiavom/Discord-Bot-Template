package arthur.silva.discordbot.base.ui.command.base;

import arthur.silva.discordbot.base.ui.command.base.requirement.Requirement;

public enum MainMenuType {

    /**
     *
     * !!! Be sure to verify all elements when updating/adding/removing elements. Some permissions can affect other menus.
     *
     * */
    USER("User",
            command -> command.getRequirementsManager().doesNotRequire(Requirement.ADMIN))

    , ADMIN("Admin",
                command -> command.getRequirementsManager().requires(Requirement.ADMIN))
    ; // TODO -- add a verifier on boot that checks if none of these overlap

    private final String NAME;
    private final MainMenuTypeAction ACTION;

    MainMenuType(String name, MainMenuTypeAction action) {
        this.NAME = name;
        this.ACTION = action;
    }

    public boolean accepts(Command command) {
        return ACTION.accepts(command);
    }

    private interface MainMenuTypeAction {
        boolean accepts(Command command);
    }
}
