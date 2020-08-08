package arthur.silva.discordbot.base.shutdown.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;

/**
 * Shuts down the application.
 */
@Service
public class ShutdownExecutor implements DisposableBean {
    private final Logger LOGGER = LoggerFactory.getLogger(ShutdownExecutor.class);

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    private Collection<ShutdownModule> shutdownModules;

    /**
     * Shuts down the application
     */
    @Override
    public final void destroy() {
        LOGGER.info("------------- SHUTDOWN STARTED -------------");

        runShutdownModules(shutdownModules);

        LOGGER.info("------------ SHUTDOWN TERMINATED ------------");
    }

    @PostConstruct
    private void setup() {
        shutdownModules = applicationContext.getBean("shutdownModules", Collection.class);

        if (!checkIfAllShutdownModulesAreRegistered())
            LOGGER.error("NOT ALL SHUTDOWN MODULES ARE REGISTERED");
    }

    /**
     * Makes sure that all modules are registered.
     */
    private boolean checkIfAllShutdownModulesAreRegistered() {
        int total = applicationContext.getBeansOfType(ShutdownModule.class).size();
        int registered = shutdownModules.size();

        return total == registered;
    }

    /**
     * Executes the shutdown modules.
     *
     * @param modules modules to execute
     */
    private void runShutdownModules(Collection<? extends ShutdownModule> modules) {
        for (ShutdownModule module : modules) {
            runShutdownModule(module);
        }
    }

    /**
     * Executes a shutdown module.
     *
     * @param module the module to execute
     */
    private void runShutdownModule(ShutdownModule module) {
        String moduleName = module.getDisplayName();
        LOGGER.info("SHUTTING DOWN: " + moduleName);
        try {
            module.execute();
        } catch (Exception e) {
            LOGGER.error("ERROR SHUTTING DOWN MODULE " + moduleName + ": " + e.getMessage());
        }
    }
}
