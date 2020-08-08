package discord.bot.boot.bootstrap.boostrappers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.PostConstruct;

public abstract class Boostrapper {
    @Autowired private ConfigurableApplicationContext applicationContext;
    private final Logger LOGGER = LoggerFactory.getLogger(Boostrapper.class);
    private final String LOGGING_MESSAGE_PREFIX = ">>>>> BOOTSTRAPPING " + getModuleDisplayName() + ": ";

    public abstract String getModuleDisplayName();
    public abstract void bootstrap() throws BootstrapException;

    @PostConstruct
    private void boot() {
        logInfo("STARTING");

        try {
            bootstrap();
        } catch (BootstrapException e) {
            logError(e.getMessage());
            if (e.shouldExit)
                applicationContext.close();
        } catch (Exception e) {
            logError(e.getMessage());
        }

        logInfo("FINISHED");
    }

    private void logInfo(String msg) {
        LOGGER.info(LOGGING_MESSAGE_PREFIX + msg);
    }

    private void logWarn(String msg) {
        LOGGER.warn(LOGGING_MESSAGE_PREFIX + msg);
    }

    private void logError(String msg) {
        LOGGER.error(LOGGING_MESSAGE_PREFIX + msg);
    }
}
