package discord.bot.boot_shutdown.bootstrap.boostrappers.implementations;

import discord.bot.boot_shutdown.bootstrap.boostrappers.Boostrapper;
import discord.bot.boot_shutdown.bootstrap.boostrappers.BootstrapException;
import discord.bot.core.card.Character;
import discord.bot.data.card.CharacterStorage;
import discord.bot.data.configuration.GlobalConfiguration;
import discord.bot.infrastructure.application.utils.exception.UserConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service("characterBootstrapper")
@DependsOn("userConfigurationBootstrapper")
public class CharacterBootstrapper extends Boostrapper {

    @Autowired
    private CharacterStorage characterStorage;

    @Override
    public String getModuleDisplayName() {
        return "Characters";
    }
    
    @Override
    public void bootstrap() throws BootstrapException {
        File folder = new File(GlobalConfiguration.Card.CHARACTER_IMAGE_DIRECTORY);
        File[] files = folder.listFiles();

        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();
                try {
                    characterStorage.addCharacter(new Character(fileName));
                } catch (Exception e) {
                    throw new BootstrapException("ERROR loading \"" + fileName + "\": " + e.getMessage(), true);
                }
            }
        }
    }
}
