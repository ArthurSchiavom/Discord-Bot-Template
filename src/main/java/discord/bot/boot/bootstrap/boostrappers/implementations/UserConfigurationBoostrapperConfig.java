package discord.bot.boot.bootstrap.boostrappers.implementations;

import discord.bot.boot.bootstrap.boostrappers.BootstrapException;
import discord.bot.data.configuration.GlobalConfiguration;

import java.awt.Color;
import java.util.Collection;

public class UserConfigurationBoostrapperConfig {

    /**
     * Process user configurations.
     *
     * @param configurationFilePath path to the configuration file
     * @param configurations loaded configurations
     */
    public static void processUserConfigurations(String configurationFilePath, Collection<UserConfiguration> configurations) throws BootstrapException {

        for (UserConfiguration config : configurations) {
            String configurationValue = config.value.trim();
            switch(config.name.toLowerCase().replaceAll(" ", "")) {
                case "token":
                    GlobalConfiguration.Bot.token = configurationValue;
                    break;
                case "commandsprefix":
                    GlobalConfiguration.Command.commandPrefix = convertQuoteMarkedConfig(configurationValue);
                    GlobalConfiguration.Command.commandPrefixNChars = GlobalConfiguration.Command.commandPrefix.length();
                    break;
                case "game":
                    GlobalConfiguration.Bot.playingStatus = configurationValue;
                    break;
                case "helpembedcolor":
                    GlobalConfiguration.Command.defaultEmbedColor = Color.decode(configurationValue);
                    break;
                case "helpembedfooterimageurl":
                    GlobalConfiguration.Command.helpEmbedFooterImageUrl = configurationValue;
                    break;
                case "helpembedfootertext":
                    GlobalConfiguration.Command.helpEmbedFooterText = configurationValue;
                    break;
                case "mainmenuthumbnail":
                    GlobalConfiguration.Command.mainMenuThumbnail = configurationValue;
            }
        }

        if (GlobalConfiguration.Bot.token == null)
            throw new BootstrapException("You must specify the bot token (token=123456789) in the configuration file: " + configurationFilePath, true);
        if (GlobalConfiguration.Command.commandPrefix == null)
            throw new BootstrapException("You must specify the command prefix (prefix=\"!\") in the configuration file: " + configurationFilePath, true);
    }

    /**
     * Retrieves a configuration value that's delimited by quotes.
     *
     * @param configuration the raw configuration value
     * @return the configuration value between quotes
     */
    private static String convertQuoteMarkedConfig(String configuration) {
        return configuration.split("\"")[1];
    }
}
