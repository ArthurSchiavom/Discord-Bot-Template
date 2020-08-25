package discord.bot.boot_shutdown.bootstrap.boostrappers;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.PostConstruct;

@Slf4j
public abstract class Boostrapper {
    @Autowired private ConfigurableApplicationContext applicationContext;
    private final String LOGGING_MESSAGE_PREFIX = ">>>>> BOOTSTRAPPING " + getModuleDisplayName() + ": ";

    public abstract String getModuleDisplayName();
    public abstract void bootstrap() throws BootstrapException;

    @PostConstruct
    private void boot() {
        logInfo("STARTED");

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
        log.info(LOGGING_MESSAGE_PREFIX + msg);
    }

    private void logWarn(String msg) {
        log.warn(LOGGING_MESSAGE_PREFIX + msg);
    }

    private void logError(String msg) {
        log.error(LOGGING_MESSAGE_PREFIX + msg);
    }
}
