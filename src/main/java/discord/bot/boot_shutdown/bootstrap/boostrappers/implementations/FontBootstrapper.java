package discord.bot.boot_shutdown.bootstrap.boostrappers.implementations;

import discord.bot.boot_shutdown.bootstrap.boostrappers.Boostrapper;
import discord.bot.boot_shutdown.bootstrap.boostrappers.BootstrapException;
import discord.bot.core.card.Frame;
import discord.bot.data.configuration.GlobalConfiguration;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.File;

@Service("fontBootstrapper")
public class FontBootstrapper extends Boostrapper {
    @Override
    public String getModuleDisplayName() {
        return "Fonts";
    }

    @Override
    public void bootstrap() throws BootstrapException {
        File folder = new File(GlobalConfiguration.Font.FONT_DIRECTORY);
        File[] files = folder.listFiles();

        for (File file : files) {
            if (file.isFile()) {
                try {
                    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                    ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(file.getPath())));
                } catch (Exception e) {
                    throw new BootstrapException("Failed to register font: " + file.getName(), true);
                }
            }
        }
    }
}
