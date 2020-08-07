package arthur.silva.discordbot.base.boot.bootstrap.base;

import arthur.silva.discordbot.base.boot.bootstrap.boostrappers.JDABootstrapper;
import arthur.silva.discordbot.base.boot.bootstrap.boostrappers.UserConfigurationLoader;
import arthur.silva.discordbot.base.data.loaded.configuration.GlobalConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Bootstrapping configuration
 */
@Configuration
public class BootConfig {

    private String configurationFilePath;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired JDABootstrapper jdaBootstrapper;
    @Autowired
    UserConfigurationLoader.UserConfigurationBootstrapper userConfigurationBootstrapper;

    /**
     * Register ordered boostrappers here.
     */
    @Bean(name="orderedBootstrappers")
    public Collection<BootstrapperOrdered> orderedBootstrappers() {
        Collection<BootstrapperOrdered> bootstrappers = new ArrayList<>();
        // Add your bootstrappers to the list above in the desired order
        bootstrappers.add(userConfigurationBootstrapper);
        bootstrappers.add(jdaBootstrapper);

        return bootstrappers;
    }

    @Bean(name="unorderedBootstrappers")
    public Collection<BootstrapperUnordered> unorderedBootstrappers() {
        return applicationContext.getBeansOfType(BootstrapperUnordered.class).values();
    }

    /**
     * Process user configurations.
     *
     * @param configurationWithLowerCaseNames map where keys are the configuration keys and the values are the respective configuration value
     */
    public static void processUserConfigurations(String configurationFilePath, Map<String, String> configurationWithLowerCaseNames) throws BootstrapException {
        String selectedConfiguration;

        selectedConfiguration = configurationWithLowerCaseNames                                               .get("token");
        if (selectedConfiguration == null)
            throw new BootstrapException("The bot token configuration (token=123456789) is missing in the configuration file: " + configurationFilePath, true);
        GlobalConfiguration.Bot.token = selectedConfiguration;

        selectedConfiguration = configurationWithLowerCaseNames                                               .get("commandsprefix");
        if (selectedConfiguration == null)
            throw new BootstrapException("The bot prefix configuration (prefix=\"! \") is missing in the configuration file: " + configurationFilePath, true);
        GlobalConfiguration.Command.commandPrefix = convertQuoteMarkedConfig(selectedConfiguration);
        GlobalConfiguration.Command.commandPrefixNChars = GlobalConfiguration.Command.commandPrefix.length();

        GlobalConfiguration.Bot.playingStatus = configurationWithLowerCaseNames                               .get("game");
        GlobalConfiguration.Command.defaultEmbedColor = Color.decode(configurationWithLowerCaseNames          .get("helpembedcolor"));
        GlobalConfiguration.Command.helpEmbedFooterImageUrl = configurationWithLowerCaseNames                 .get("helpembedfooterimageurl");
        GlobalConfiguration.Command.helpEmbedFooterText = configurationWithLowerCaseNames                     .get("helpembedfootertext");
        GlobalConfiguration.Command.mainMenuThumbnail = configurationWithLowerCaseNames                       .get("mainmenuthumbnail");
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
