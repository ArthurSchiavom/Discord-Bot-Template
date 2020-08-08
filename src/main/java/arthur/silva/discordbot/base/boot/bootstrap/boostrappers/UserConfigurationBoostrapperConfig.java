package arthur.silva.discordbot.base.boot.bootstrap.boostrappers;

import arthur.silva.discordbot.base.boot.bootstrap.base.BootstrapException;
import arthur.silva.discordbot.base.data.loaded.configuration.GlobalConfiguration;

import java.awt.*;
import java.util.Map;

public class UserConfigurationBoostrapperConfig {
    /**
     * Process user configurations.
     *
     * @param configurationNames map where keys are the configuration keys and the values are the respective configuration value. Keys must be lowercase and without spaces.
     */
    public static void processUserConfigurations(String configurationFilePath, Map<String, String> configurationNames) throws BootstrapException {
        String selectedConfiguration;

        selectedConfiguration = configurationNames                                               .get("token");
        if (selectedConfiguration == null)
            throw new BootstrapException("The bot token configuration (token=123456789) is missing in the configuration file: " + configurationFilePath, true);
        GlobalConfiguration.Bot.token = selectedConfiguration;

        selectedConfiguration = configurationNames                                               .get("commandsprefix");
        if (selectedConfiguration == null)
            throw new BootstrapException("The bot prefix configuration (prefix=\"! \") is missing in the configuration file: " + configurationFilePath, true);
        GlobalConfiguration.Command.commandPrefix = convertQuoteMarkedConfig(selectedConfiguration);
        GlobalConfiguration.Command.commandPrefixNChars = GlobalConfiguration.Command.commandPrefix.length();

        GlobalConfiguration.Bot.playingStatus = configurationNames                               .get("game");
        GlobalConfiguration.Command.defaultEmbedColor = Color.decode(configurationNames          .get("helpembedcolor"));
        GlobalConfiguration.Command.helpEmbedFooterImageUrl = configurationNames                 .get("helpembedfooterimageurl");
        GlobalConfiguration.Command.helpEmbedFooterText = configurationNames                     .get("helpembedfootertext");
        GlobalConfiguration.Command.mainMenuThumbnail = configurationNames                       .get("mainmenuthumbnail");
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

    /**
     * Indicates an exception while loading user configurations.
     */
    public static class ConfigurationException extends Exception {
        public ConfigurationException(String message) {
            super(message);
        }
    }
}
