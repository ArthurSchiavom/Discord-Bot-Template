package arthur.silva.discordbot.base.boot.bootstrap.base;

import arthur.silva.discordbot.base.boot.bootstrap.boostrappers.JDABootstrapper;
import arthur.silva.discordbot.base.boot.bootstrap.boostrappers.UserConfigurationBootstrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collection;

@Configuration
public class BootConfig {

    @Autowired
    ApplicationContext applicationContext;

    @Bean(name="orderedBootstrappers")
    public Collection<BootstrapperOrdered> orderedBootstrappers() {
        Collection<BootstrapperOrdered> bootstrappers = new ArrayList<>();
        // Add your bootstrappers to the list above in the desired order
        bootstrappers.add(new UserConfigurationBootstrapper());
        bootstrappers.add(new JDABootstrapper());

        return bootstrappers;
    }

    @Bean(name="unorderedBootstrappers")
    public Collection<BootstrapperUnordered> unorderedBootstrappers() {
        return applicationContext.getBeansOfType(BootstrapperUnordered.class).values();
    }

}
