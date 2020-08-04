package arthur.silva.discordbot.base.boot.bootstrap.boostrappers;

import arthur.silva.discordbot.base.boot.bootstrap.base.BootstrapException;
import arthur.silva.discordbot.base.boot.bootstrap.base.BootstrapperOrdered;
import arthur.silva.discordbot.base.data.loaded.configuration.GlobalConfiguration;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

public class JDABootstrapper implements BootstrapperOrdered {
    @Override
    public void boot() throws BootstrapException {
        try {
            GlobalConfiguration.Bot.jda = JDABuilder.createDefault(GlobalConfiguration.Bot.token).build().awaitReady();
            if (GlobalConfiguration.Bot.playingStatus != null)
                GlobalConfiguration.Bot.jda.getPresence().setActivity(Activity.playing(GlobalConfiguration.Bot.playingStatus));
        } catch (Exception e) {
            throw new BootstrapException("FAILED TO LOG-IN THE BOT: " + e.getMessage(), true);
        }
    }

    @Override
    public String loadingTargetName() {
        return "Discord API Framework";
    }
}
