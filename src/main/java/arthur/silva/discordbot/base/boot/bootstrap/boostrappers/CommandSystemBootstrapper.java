package arthur.silva.discordbot.base.boot.bootstrap.boostrappers;

import arthur.silva.discordbot.base.boot.bootstrap.base.BootstrapException;
import arthur.silva.discordbot.base.boot.bootstrap.base.BootstrapperOrdered;

public class CommandSystemBootstrapper implements BootstrapperOrdered {
    @Override
    public void boot() throws BootstrapException {

    }

    @Override
    public String loadingTargetName() {
        return "Command System";
    }
}
