package arthur.silva.discordbot.base.boot.bootstrap.base;

/**
 * Bootstraps a particular module.
 */
public interface Bootstrapper {
    /**
     * Bootstraps this module.
     *
     * @throws BootstrapException in case an error occurs.
     */
    void boot() throws BootstrapException;

    /**
     * @return this bootstrapper's module's display name.
     */
    String getModuleDisplayName();
}
