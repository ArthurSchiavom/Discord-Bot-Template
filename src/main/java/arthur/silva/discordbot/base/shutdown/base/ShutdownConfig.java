package arthur.silva.discordbot.base.shutdown.base;

import arthur.silva.discordbot.base.shutdown.modules.JDAShutdown;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Configuration for the shutdown system
 */
@Configuration
public class ShutdownConfig {

    @Autowired private JDAShutdown jdaShutdown;

    @Bean("shutdownModules")
    public Collection<ShutdownModule> shutdownModules() {
        ArrayList<ShutdownModule> shutdownModules = new ArrayList<>();
        shutdownModules.add(jdaShutdown);

        return shutdownModules;
    }
}
