package arthur.silva.discordbot.base.boot.bootstrap.boostrappers;

import arthur.silva.discordbot.base.boot.bootstrap.base.BootstrapException;
import arthur.silva.discordbot.base.boot.bootstrap.base.BootstrapperOrdered;
import lombok.Cleanup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserConfigurationBootstrapper implements BootstrapperOrdered {

    @Value("${user.configuration.file}")
    private String configurationFilePath;

    @Override
    public void boot() throws BootstrapException {
        Map<String, String> configurationMap;
        try {
            configurationMap = load(configurationFilePath);
        } catch (UserConfigurationBoostrapperConfig.ConfigurationException e) {
            throw new BootstrapException("The configuration file (" + configurationFilePath + ") couldn't be read: " + e.getMessage(), true);
        }

        configure(configurationMap);
    }

    private void configure(Map<String, String> configurations) throws BootstrapException {
        UserConfigurationBoostrapperConfig.processUserConfigurations(configurationFilePath, configurations);
    }

    @Override
    public String getModuleDisplayName() {
        return "User Configuration";
    }

    public Map<String, String> load(String configurationFilePath) throws UserConfigurationBoostrapperConfig.ConfigurationException {
        HashMap<String, String> result = new HashMap<>();
        int nLine = 0;
        try {
            @Cleanup FileReader fileReader = new FileReader(configurationFilePath, Charset.forName(StandardCharsets.UTF_8.name()));
            @Cleanup BufferedReader br = new BufferedReader(fileReader);
            String str;
            while ((str = br.readLine()) != null) {
                nLine++;
                if (!str.startsWith("#") && str.contains("=")) {
                    String[] config = str.split("=");
                    config[0] = config[0].toLowerCase().replace(" ", "");
                    config[1] = config[1].trim();
                    result.put(config[0], config[1]);
                }
            }

        } catch (FileNotFoundException e) {
            throw new UserConfigurationBoostrapperConfig.ConfigurationException("The configuration file " + configurationFilePath + " is not present. Please add it to the config folder.");
        } catch (Exception e) {
            throw new UserConfigurationBoostrapperConfig.ConfigurationException("ERROR READING THE CONFIGURATION FILE. LINE " + nLine + ". Details: " + e.getMessage());
        }
        return result;
    }
}

