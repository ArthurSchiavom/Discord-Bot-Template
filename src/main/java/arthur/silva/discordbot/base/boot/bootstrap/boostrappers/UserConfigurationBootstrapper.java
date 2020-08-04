package arthur.silva.discordbot.base.boot.bootstrap.boostrappers;

import arthur.silva.discordbot.base.boot.bootstrap.base.BootstrapException;
import arthur.silva.discordbot.base.boot.bootstrap.base.BootstrapperOrdered;
import arthur.silva.discordbot.base.data.loaded.configuration.GlobalConfiguration;
import arthur.silva.discordbot.base.infrastructure.application.user.configuration.ConfigurationException;
import arthur.silva.discordbot.base.infrastructure.application.user.configuration.UserConfigurationLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Map;

@Service
public class UserConfigurationBootstrapper implements BootstrapperOrdered {

    @Value("${user.configuration.file}")
    private String configurationFilePath;

    @Override
    public void boot() throws BootstrapException {
        UserConfigurationLoader configurationLoader = new UserConfigurationLoader(configurationFilePath);
        Map<String, String> configuration;
        try {
            configuration = configurationLoader.load();
        } catch (ConfigurationException e) {
            throw new BootstrapException("The configuration file (" + configurationFilePath + ") couldn't be read: " + e.getMessage(), true);
        }

        configure(configuration);
    }

    private void configure(Map<String, String> configuration) throws BootstrapException {
        String selectedConfiguration;

        selectedConfiguration = configuration                                   .get("token");
        if (selectedConfiguration == null)
            throw new BootstrapException("The bot token configuration (token=123) is missing in the configuration file: " + configurationFilePath, true);
        GlobalConfiguration.Bot.token = selectedConfiguration;

        selectedConfiguration = configuration                                   .get("commandsprefix");
        if (selectedConfiguration == null)
            throw new BootstrapException("The bot prefix configuration (prefix=\"! \") is missing in the configuration file: " + configurationFilePath, true);
        GlobalConfiguration.Command.commandPrefix = convertQuoteMarkedConfig(selectedConfiguration);
        GlobalConfiguration.Command.commandPrefixNChars = GlobalConfiguration.Command.commandPrefix.length();

        GlobalConfiguration.Bot.playingStatus = configuration                                       .get("game");
        GlobalConfiguration.Command.defaultEmbedColor = Color.decode(configuration        .get("helpembedcolor"));
        GlobalConfiguration.Command.helpEmbedFooterImageUrl = configuration               .get("helpembedfooterimageurl");
        GlobalConfiguration.Command.helpEmbedFooterText = configuration                   .get("helpembedfootertext");
        GlobalConfiguration.Command.mainMenuThumbnail = configuration                     .get("mainmenuthumbnail");
    }

    private static String convertQuoteMarkedConfig(String cfg) {
        return cfg.split("\"")[1];
    }

    @Override
    public String loadingTargetName() {
        return "User Configuration";
    }
}
