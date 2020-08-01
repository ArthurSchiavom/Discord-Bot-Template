package arthur.silva.discordbot.base.boot.bootstrap.base;

public interface Bootstrapper {
    void boot() throws BootstrapException;
    String loadingTargetName();
}
