package arthur.silva.discordbot.base.shutdown.implementations;

import arthur.silva.discordbot.base.data.loaded.configuration.GlobalConfiguration;
import arthur.silva.discordbot.base.shutdown.ModuleShutdown;
import arthur.silva.discordbot.base.shutdown.ShutdownException;
import net.dv8tion.jda.api.JDA;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Shutdown JDA
 */
@Service("jdaModuleShutdown")
public class JDAModuleShutdown extends ModuleShutdown {
    @Override
    public void shutdown() throws ShutdownException {
        JDA jda = GlobalConfiguration.Bot.jda;
        if (jda != null) {
            jda.shutdownNow();
        }
    }

    @Override
    public String getModuleDisplayName() {
        return "Discord API Framework";
    }
}
