package arthur.silva.discordbot.base.ui.command;

import arthur.silva.discordbot.base.ui.command.base.Category;
import arthur.silva.discordbot.base.ui.command.base.CommandWithSubcommands;
import arthur.silva.discordbot.base.ui.command.base.requirement.Requirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SubTest extends CommandWithSubcommands {
    @Autowired
    private SubsSub child;

    public SubTest() {
        super(Category.FUN, "Some cool description!!!!");
    }

    @PostConstruct
    public void init() {
        this.registerName("Daddy");
        this.registerName("Papa");
//        this.registerUsageExample(new CommandUsageExample("arg1 arg2", "nothing because this is a test"));
//        this.registerUsageExample(new CommandUsageExample("", "!nothing because this is a test!"));
        this.registerRequirements(Requirement.ADMIN);
        this.registerSubcommands(child);
    }
}
