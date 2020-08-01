package arthur.silva.discordbot.base.boot.bootstrap.base;

/**
 * <b><u>!!!! These bootstrappers must be registered at {@link BootConfig#orderedBootstrappers()} !!!!</u></b>
 * <br>
 * <br>Bootstrapper that must be executed in a specific order relatively to other bootstrappers. All ordered bootstrappers execute before unordered bootstrappers.
 *
 * @see BootConfig#orderedBootstrappers()
 */
public interface BootstrapperOrdered extends Bootstrapper {
}
