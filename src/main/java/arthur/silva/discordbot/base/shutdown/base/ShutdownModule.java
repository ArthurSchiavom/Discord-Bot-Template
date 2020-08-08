package arthur.silva.discordbot.base.shutdown.base;

public interface ShutdownModule {
    void execute() throws ShutdownException;
    String getDisplayName();
}
