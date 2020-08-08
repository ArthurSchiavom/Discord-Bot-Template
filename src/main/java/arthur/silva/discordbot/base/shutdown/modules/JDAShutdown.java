package arthur.silva.discordbot.base.shutdown.modules;

import arthur.silva.discordbot.base.data.loaded.configuration.GlobalConfiguration;
import arthur.silva.discordbot.base.shutdown.base.ShutdownException;
import arthur.silva.discordbot.base.shutdown.base.ShutdownModule;
import net.dv8tion.jda.api.JDA;
import org.springframework.stereotype.Component;

/**
 * Shutdown JDA
 */
@Component
public class JDAShutdown implements ShutdownModule {
    @Override
    public void execute() throws ShutdownException {
        JDA jda = GlobalConfiguration.Bot.jda;
        if (jda != null) {
            jda.shutdownNow();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            jda.shutdownNow();
        }
    }

    @Override
    public String getDisplayName() {
        return "Discord API Framework";
    }
}
