package arthur.silva.discordbot.base.boot.bootstrap.boostrappers.implementations;

import arthur.silva.discordbot.base.boot.bootstrap.boostrappers.Boostrapper;
import arthur.silva.discordbot.base.boot.bootstrap.boostrappers.BootstrapException;
import arthur.silva.discordbot.base.ui.events.EventsManager;
import arthur.silva.discordbot.base.data.loaded.configuration.GlobalConfiguration;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

@Service("jdaBootstrapper")
@DependsOn(value = {"userConfigurationBootstrapper", "jdaModuleShutdown"})
public class JDABootstrapper extends Boostrapper {

    @Autowired
    private EventsManager eventsManager;

    @Override
    public void bootstrap() throws BootstrapException {
        try {
            GlobalConfiguration.Bot.jda = JDABuilder.createDefault(GlobalConfiguration.Bot.token).build().awaitReady();

            if (GlobalConfiguration.Bot.playingStatus != null)
                GlobalConfiguration.Bot.jda.getPresence().setActivity(Activity.playing(GlobalConfiguration.Bot.playingStatus));

            GlobalConfiguration.Bot.jda.addEventListener(eventsManager);
        } catch (Exception e) {
            throw new BootstrapException("FAILED TO LOG-IN THE BOT: " + e.getMessage(), true);
        }
    }

    @Override
    public String getModuleDisplayName() {
        return "Discord API Framework";
    }
}
