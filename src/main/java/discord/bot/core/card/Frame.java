package discord.bot.core.card;

import discord.bot.data.configuration.GlobalConfiguration;
import discord.bot.infrastructure.application.utils.ImageUtils;
import discord.bot.infrastructure.application.utils.exception.UserConfigurationException;
import discord.bot.infrastructure.image.Area;
import discord.bot.infrastructure.image.TwoDPosition;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class Frame {
    private static final String SEPARATOR = ";";

    private final String fileName;
    public final String frameName;
    public final boolean isDefaultFrame;
    private final Area characterArea;
    private final Area characterNameArea;
    private final Area animeNameArea;

    public Frame(String imageFileName, boolean isDefaultFrame) throws UserConfigurationException, IOException {
        this.isDefaultFrame = isDefaultFrame;
        this.fileName = imageFileName;
        String[] fileNameSplit = imageFileName.split(SEPARATOR, 2);
//        if (fileNameSplit.length != 2) {
//            throw new UserConfigurationException(
//                    "The frame file name \"" + imageFileName + "\" is missing information. " +
//                            "It should include the frame name and the number of pieces, " +
//                            "separated by \"" + SEPARATOR + "\"");
//        }

        frameName = fileNameSplit[0];
        BufferedImage bufferedImage = retrieveImageRaw();
        characterArea = Area.findAreaDelimitedBy(bufferedImage, GlobalConfiguration.Card.characterImageAreaDelimiterColor);
        characterNameArea = Area.findAreaDelimitedBy(bufferedImage, GlobalConfiguration.Card.characterNameAreaDelimiterColor);
        animeNameArea = Area.findAreaDelimitedBy(bufferedImage, GlobalConfiguration.Card.animeNameAreaDelimiterColor);
    }

    public BufferedImage retrieveImage() throws IOException {
        BufferedImage image = retrieveImageRaw();
        replaceMarkerPixel(image, characterArea.topLeft, 1);
        replaceMarkerPixel(image, characterArea.bottomRight, -1);
        replaceMarkerPixel(image, characterNameArea.topLeft, 1);
        replaceMarkerPixel(image, characterNameArea.bottomRight, -1);
        replaceMarkerPixel(image, animeNameArea.topLeft, 1);
        replaceMarkerPixel(image, animeNameArea.bottomRight, -1);
        return image;
    }

    private BufferedImage retrieveImageRaw() throws IOException {
        return ImageIO.read(new File(getDirectoryPath(), fileName));
    }

    public BufferedImage retrieveImage(Character character) throws IOException {
        BufferedImage characterImage = character.retrieveImage();
        BufferedImage result = fitImageIntoFrame(characterImage);
        addCharacterName(result, character);
        addAnimeName(result, character);
        return result;
    }

    private void addCharacterName(BufferedImage imageSoFar, Character character) throws IOException {
        ImageUtils.addText(imageSoFar, character.characterName,
                GlobalConfiguration.Card.characterNameFont, Font.PLAIN, 0.5d, GlobalConfiguration.Card.characterNameColor,
                characterNameArea);
    }

    private void addAnimeName(BufferedImage imageSoFar, Character character) throws IOException {
        ImageUtils.addText(imageSoFar, character.animeName,
                GlobalConfiguration.Card.animeNameFont, Font.PLAIN, 0.5d, GlobalConfiguration.Card.animeNameColor,
                animeNameArea);
    }

    private BufferedImage fitImageIntoFrame(BufferedImage image) throws IOException {
        BufferedImage frameImage = retrieveImage();

        int frameWidth = frameImage.getWidth();
        int frameHeight = frameImage.getHeight();
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        int imageSlotWidth = characterArea.bottomRight.x + 1 - characterArea.topLeft.x;
        int imageSlotHeight = characterArea.bottomRight.y + 1 - characterArea.topLeft.y;
        double widthScale = imageSlotWidth / (double) imageWidth;
        double heightScale = imageSlotHeight / (double) imageHeight;
        double chosenScale = Math.max(widthScale, heightScale);
        int scaledImageWidth = (int) (imageWidth * chosenScale);
        int scaledImageHeight = (int) (imageHeight * chosenScale);
        if (scaledImageWidth < imageSlotWidth)
            scaledImageWidth = imageSlotWidth;
        if (scaledImageHeight < imageSlotHeight)
            scaledImageHeight = imageSlotHeight;

        Image scaledImage = image.getScaledInstance(
                scaledImageWidth,
                scaledImageHeight,
                Image.SCALE_SMOOTH);

        BufferedImage combined = new BufferedImage(frameWidth, frameHeight, BufferedImage.TYPE_INT_ARGB);
        int startY = (scaledImageHeight - imageSlotHeight) / 2;
        int endY = startY + Math.min(imageSlotHeight, scaledImage.getHeight(null));
        int startX = (scaledImageWidth - imageSlotWidth) / 2;
        int endX = startX + Math.min(imageSlotWidth, scaledImage.getWidth(null));

        Graphics g = combined.getGraphics();
        g.drawImage(scaledImage, characterArea.topLeft.x, characterArea.topLeft.y,
                characterArea.bottomRight.x + 1, characterArea.bottomRight.y + 1,
                startX, startY, endX, endY, null);
        g.drawImage(frameImage, 0, 0, null);
        g.dispose();

        return combined;
    }

    public byte[] retrieveImageBytesPng(Character character) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(retrieveImage(character), "PNG", baos);
        return baos.toByteArray();
    }

    private void replaceMarkerPixel(BufferedImage bufferedImage, TwoDPosition pos, int xDislocation) {
        int pixelToTheRight = bufferedImage.getRGB(pos.x + xDislocation, pos.y);
        bufferedImage.setRGB(pos.x, pos.y, pixelToTheRight);
    }

    private String getDirectoryPath() {
        return isDefaultFrame ? GlobalConfiguration.Card.DEFAULT_FRAMES_IMAGE_DIRECTORY : GlobalConfiguration.Card.FRAME_IMAGE_DIRECTORY;
    }
}
