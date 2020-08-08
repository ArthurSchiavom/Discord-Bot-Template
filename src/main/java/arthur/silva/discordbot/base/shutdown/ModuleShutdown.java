package arthur.silva.discordbot.base.shutdown;

import arthur.silva.discordbot.base.boot.bootstrap.boostrappers.Boostrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;

public abstract class ModuleShutdown implements DisposableBean {
    private final Logger LOGGER = LoggerFactory.getLogger(Boostrapper.class);
    private final String LOGGING_MESSAGE_PREFIX = ">>>>> MODULE SHUTDOWN " + getModuleDisplayName() + ": ";

    public abstract String getModuleDisplayName();
    public abstract void shutdown() throws ShutdownException;

    @Override
    public void destroy() throws Exception {
        logInfo("STARTING");

        try {
            shutdown();
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
