package arthur.silva.discordbot.base.boot.bootstrap.base;

import arthur.silva.discordbot.base.boot.bootstrap.boostrappers.JDABootstrapper;
import arthur.silva.discordbot.base.boot.bootstrap.boostrappers.UserConfigurationBootstrapper;
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
    @Autowired UserConfigurationBootstrapper userConfigurationBootstrapper;

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
}
