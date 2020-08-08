package discord.bot.shutdown.implementations;

import discord.bot.data.configuration.GlobalConfiguration;
import discord.bot.shutdown.ModuleShutdown;
import discord.bot.shutdown.ShutdownException;
import net.dv8tion.jda.api.JDA;
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
