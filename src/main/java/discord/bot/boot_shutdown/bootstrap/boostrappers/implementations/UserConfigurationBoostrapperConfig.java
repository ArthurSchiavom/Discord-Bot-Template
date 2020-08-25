package discord.bot.boot_shutdown.bootstrap.boostrappers.implementations;

import discord.bot.boot_shutdown.bootstrap.boostrappers.BootstrapException;
import discord.bot.data.configuration.GlobalConfiguration;

import java.awt.*;
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
                    break;
                case "characterimageareadelimitercolorhex":
                    GlobalConfiguration.Card.characterImageAreaDelimiterColor = extractColor(configurationValue);
                    break;
                case "characternameareadelimitercolorhex":
                    GlobalConfiguration.Card.characterNameAreaDelimiterColor = extractColor(configurationValue);
                    break;
                case "animenameareadelimitercolorhex":
                    GlobalConfiguration.Card.animeNameAreaDelimiterColor = extractColor(configurationValue);
                    break;
                case "animenamecolor":
                    GlobalConfiguration.Card.animeNameColor = extractColor(configurationValue);
                    break;
                case "animenamefont":
                    GlobalConfiguration.Card.animeNameFont = configurationValue;
                    break;
                case "characternamecolor":
                    GlobalConfiguration.Card.characterNameColor = extractColor(configurationValue);
                    break;
                case "characternamefont":
                    GlobalConfiguration.Card.characterNameFont = configurationValue;
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

    private static Color extractColor(String colorString) throws BootstrapException {
        try {
            return Color.decode(colorString);
        } catch (Exception e) {
            throw new BootstrapException(colorString + " is not a valid hex color.", true);
        }
    }
}
