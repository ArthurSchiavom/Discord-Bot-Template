package discord.bot.boot_shutdown.bootstrap.boostrappers.implementations;

import discord.bot.boot_shutdown.bootstrap.boostrappers.Boostrapper;
import discord.bot.boot_shutdown.bootstrap.boostrappers.BootstrapException;
import discord.bot.core.card.Frame;
import discord.bot.data.card.CharacterStorage;
import discord.bot.data.card.FrameStorage;
import discord.bot.data.configuration.GlobalConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.File;
import java.io.IOException;

@Service
@DependsOn({"userConfigurationBootstrapper", "fontBootstrapper", "characterBootstrapper"})
public class FrameBootstrapper extends Boostrapper {

    @Autowired
    private FrameStorage frameStorage;

    //Todo remove
    @Autowired
    private CharacterStorage characterStorage;

    @Override
    public String getModuleDisplayName() {
        return "Frames";
    }

    @Override
    public void bootstrap() throws BootstrapException {
        File folder = new File(GlobalConfiguration.Card.FRAME_IMAGE_DIRECTORY);
        File[] files = folder.listFiles();

        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();
                try {
                    frameStorage.addFrame(new Frame(fileName, false));
                } catch (Exception e) {
                    throw new BootstrapException("ERROR loading \"" + fileName + "\": " + e.getMessage(), true);
                }
            }
        }

        folder = new File(GlobalConfiguration.Card.DEFAULT_FRAMES_IMAGE_DIRECTORY);
        files = folder.listFiles();

        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();
                try {
                    Frame frame = new Frame(fileName, true);
                    frameStorage.addDefaultFrame(frame);

                    // TODO - REMOVE
                    // TODO - REMOVE
                    // TODO - REMOVE
                    // TODO - REMOVE

                    byte[] bytes = frame.retrieveImageBytesPng(characterStorage.getCharacters().iterator().next());
                    GlobalConfiguration.Bot.jda.getTextChannelById(410814245639290880L)
                            .sendFile(bytes, "aaaa.png").queue();

                    // TODO - REMOVE
                    // TODO - REMOVE
                    // TODO - REMOVE
                    // TODO - REMOVE


                } catch (Exception e) {
                    e.printStackTrace();
                    throw new BootstrapException("ERROR loading \"" + fileName + "\": " + e.getMessage(), true);
                }
            }
        }
    }
}
