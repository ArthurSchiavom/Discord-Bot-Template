package discord.bot.bootstrap.boostrappers.implementations;

import discord.bot.bootstrap.boostrappers.Boostrapper;
import discord.bot.bootstrap.boostrappers.BootstrapException;
import lombok.Cleanup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Bootstraps the user configuration files.
 */
@Service("userConfigurationBootstrapper")
public class UserConfigurationBootstrapper extends Boostrapper {

    @Value("${user.configuration.file}")
    private String configurationFilePath;

    @Override
    public void bootstrap() throws BootstrapException {
        UserConfigurationLoader configurationLoader = new UserConfigurationLoader(configurationFilePath);
        List<UserConfiguration> configurations;
        try {
            configurations = configurationLoader.load();
        } catch (ConfigurationException e) {
            throw new BootstrapException("The configuration file (" + configurationFilePath + ") couldn't be read: " + e.getMessage(), true);
        }

        configure(configurations);
    }

    private void configure(Collection<UserConfiguration> configurations) throws BootstrapException {
        UserConfigurationBoostrapperConfig.processUserConfigurations(configurationFilePath, configurations);
    }

    @Override
    public String getModuleDisplayName() {
        return "User Configuration";
    }

    public static class UserConfigurationLoader {
        private final String CONFIG_FILE_NAME;

        public UserConfigurationLoader(String CONFIG_FILE_NAME) {
            this.CONFIG_FILE_NAME = CONFIG_FILE_NAME;
        }

        public List<UserConfiguration> load() throws ConfigurationException {
            List<UserConfiguration> result = new ArrayList<>();
            int nLine = 0;
            try {
                @Cleanup FileReader fileReader = new FileReader(CONFIG_FILE_NAME, Charset.forName(StandardCharsets.UTF_8.name()));
                @Cleanup BufferedReader br = new BufferedReader(fileReader);
                String str;
                while((str = br.readLine())!= null) {
                    nLine++;
                    if (!str.startsWith("#") && str.contains("=")) {
                        String[] config = str.split("=");
                        result.add(new UserConfiguration(config[0], config[1]));
                    }
                }

            } catch (FileNotFoundException e) {
                throw new ConfigurationException("The configuration file " + CONFIG_FILE_NAME + " is not present. Please add it to the config folder.");
            } catch (Exception e) {
                throw new ConfigurationException("ERROR READING THE CONFIGURATION FILE. LINE " + nLine + ". Details: " + e.getMessage());
            }
            return result;
        }
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
