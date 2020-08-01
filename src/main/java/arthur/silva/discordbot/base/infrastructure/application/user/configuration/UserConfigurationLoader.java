package arthur.silva.discordbot.base.infrastructure.application.user.configuration;

import lombok.Cleanup;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


public class UserConfigurationLoader {
    private final String CONFIG_FILE_NAME;

    public UserConfigurationLoader(String CONFIG_FILE_NAME) {
        this.CONFIG_FILE_NAME = CONFIG_FILE_NAME;
    }

    public Map<String, String> load() throws ConfigurationException {
        HashMap<String, String> result = new HashMap<>();
        int nLine = 0;
        try {
            @Cleanup FileReader fileReader = new FileReader(CONFIG_FILE_NAME, Charset.forName(StandardCharsets.UTF_8.name()));
            @Cleanup BufferedReader br = new BufferedReader(fileReader);
            String str;
            while((str = br.readLine())!= null) {
                nLine++;
                if (!str.startsWith("#") && str.contains("=")) {
                    String[] config = str.split("=");
                    config[0] = config[0].toLowerCase().replace(" ", "");
                    config[1] = config[1].trim();
                    result.put(config[0], config[1]);
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
