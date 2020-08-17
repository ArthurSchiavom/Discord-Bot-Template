package discord.bot.boot_shutdown.bootstrap.boostrappers.implementations;

import discord.bot.boot_shutdown.bootstrap.boostrappers.Boostrapper;
import discord.bot.boot_shutdown.bootstrap.boostrappers.BootstrapException;
import discord.bot.data.configuration.GlobalConfiguration;
import discord.bot.ui.events.EventsManager;
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
