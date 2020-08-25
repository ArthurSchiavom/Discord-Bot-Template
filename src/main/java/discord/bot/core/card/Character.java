package discord.bot.core.card;

import discord.bot.data.configuration.GlobalConfiguration;
import discord.bot.infrastructure.application.utils.exception.UserConfigurationException;
import discord.bot.infrastructure.image.Area;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Character {
    private static final String SEPARATOR = ";";

    private final String fileName;
    public final String characterName;
    public final String animeName;

    /**
     *
     * @param imageFileName
     * @throws UserConfigurationException
     */
    public Character(String imageFileName) throws UserConfigurationException {
        this.fileName = imageFileName;
        String[] fileNameSplit = imageFileName.split(SEPARATOR, 2);
        if (fileNameSplit.length != 2) {
            throw new UserConfigurationException(
                    "The card file name \"" + imageFileName + "\" is missing information. " +
                            "It should include the character name and the anime name, " +
                            "separated by \"" + SEPARATOR + "\"");
        }

        characterName = fileNameSplit[0].trim();
        animeName = fileNameSplit[1].split("\\.")[0].trim();
    }

    public BufferedImage retrieveImage() throws IOException {
        return ImageIO.read(new File(GlobalConfiguration.Card.CHARACTER_IMAGE_DIRECTORY, fileName));
    }
}
