package arthur.silva.discordbot.base.boot.bootstrap.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

@Service
public class Bootstrap {
    private final Logger LOGGER = LoggerFactory.getLogger(Bootstrap.class);

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    private Collection<BootstrapperOrdered> orderedBootstrappers;
    private Collection<BootstrapperUnordered> unorderedBootstrappers;

    /**
     * Required to guarantee that the applicationContext variable is injected before it's usage and to make sure that the bean is loaded via it's annotated name.
     */
    private final void setup() {
        orderedBootstrappers = applicationContext.getBean("orderedBootstrappers", Collection.class);
        unorderedBootstrappers = applicationContext.getBean("unorderedBootstrappers", Collection.class);
    }

    /**
     * @return (1) true in case of success and (2) false if the application should shutdown
     */
    @PostConstruct
    public final void boot() {
        LOGGER.info("------------- LOADING STARTED -------------");

        boolean allBootstrappersRegistered = true;
        boolean bootstrappingSuccessful = false;

        setup();

        if (!checkIfAllBootstrappersAreRegistered()) {
            LOGGER.error("ERROR: NOT ALL BOOTSTRAPPERS ARE REGISTERED");
            allBootstrappersRegistered = false;
        }

        if (allBootstrappersRegistered) {
            bootstrappingSuccessful = runBootstrappers();
        }

        if (!bootstrappingSuccessful) {
            LOGGER.error("-------------- LOADING FAILED --------------" +
                    "\n------------- ABORTING STARTUP -------------");
            applicationContext.close();
            return;
        }

        LOGGER.info("------------ LOADING SUCCESSFUL ------------");
    }

    /**
     * @return (1) true in case of success or (2) false if not all bootstrappers are registered.
     */
    private final boolean checkIfAllBootstrappersAreRegistered() {
        int nOrderedBootstrappersTotal = applicationContext.getBeansOfType(BootstrapperOrdered.class).size();
        int nOrderedBootstrappersRegistered = orderedBootstrappers.size();
        return nOrderedBootstrappersTotal == nOrderedBootstrappersRegistered;
    }

    /**
     * @return (1) true in case of success or (2) false if a critical bootstrapping operation failed, meaning that the application should shutdown.
     */
    private boolean runBootstrappers() {
        return runBootstrapperLists(orderedBootstrappers, unorderedBootstrappers);
    }

    /**
     * The bootstrappers are executed in order.
     *
     * @return (1) true in case of success or (2) false if a critical bootstrapping operation failed, meaning that the application should shutdown.
     */
    private boolean runBootstrapperLists(Collection<? extends Bootstrapper>... bootrappersDoubleList) {
        boolean success = true;
        for (Collection<? extends Bootstrapper> bootrappers : bootrappersDoubleList) {
            for (Bootstrapper bootstrapper : bootrappers) {
                success = runBootstrapper(bootstrapper);
                if (!success)
                    break;
            }
            if (!success)
                break;
        }

        return success;
    }

    /**
     * @return (1) true in case of success or (2) false if a critical bootstrapping operation failed, meaning that the application should shutdown.
     */
    private boolean runBootstrapperList(Collection<Bootstrapper> bootrappers) {
        boolean success = true;
        for (Bootstrapper bootstrapper : bootrappers) {
            success = runBootstrapper(bootstrapper);
            if (!success) {
                break;
            }
        }

        return success;
    }

    /**
     * @return (1) true in case of success or (2) false if a critical bootstrapping operation failed, meaning that the application should shutdown.
     */
    private boolean runBootstrapper(Bootstrapper bootstrapper) {
        String bootstrapperName = bootstrapper.loadingTargetName();
        LOGGER.info("LOADING: " + bootstrapperName);
        try {
            bootstrapper.boot();
        } catch (BootstrapException e) {
            LOGGER.error("ERROR LOADING " + bootstrapperName + ": " + e.getMessage());
            if (e.shouldExit) {
                return false;
            }
        }
        return true;
    }
}
