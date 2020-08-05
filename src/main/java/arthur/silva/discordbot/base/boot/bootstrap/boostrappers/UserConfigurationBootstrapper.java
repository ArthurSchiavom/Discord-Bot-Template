package arthur.silva.discordbot.base.boot.bootstrap.boostrappers;

import arthur.silva.discordbot.base.boot.bootstrap.base.BootConfig;
import arthur.silva.discordbot.base.boot.bootstrap.base.BootstrapException;
import arthur.silva.discordbot.base.boot.bootstrap.base.BootstrapperOrdered;
import arthur.silva.discordbot.base.infrastructure.application.user.configuration.ConfigurationException;
import arthur.silva.discordbot.base.infrastructure.application.user.configuration.UserConfigurationLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    private void configure(Map<String, String> configurations) throws BootstrapException {
        BootConfig.processUserConfigurations(configurationFilePath, configurations);
    }

    private static String convertQuoteMarkedConfig(String cfg) {
        return cfg.split("\"")[1];
    }

    @Override
    public String loadingTargetName() {
        return "User Configuration";
    }
}
