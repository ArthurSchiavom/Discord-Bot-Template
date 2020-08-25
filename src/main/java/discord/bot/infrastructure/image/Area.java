package discord.bot.infrastructure.image;

import discord.bot.infrastructure.application.utils.ImageUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;
import java.awt.image.BufferedImage;

@AllArgsConstructor
public class Area {
    public final TwoDPosition topLeft;
    public final TwoDPosition bottomRight;

    public static Area findAreaDelimitedBy(BufferedImage image, Color color) {
        int pixelToFind = ImageUtils.colorToInt(color);
        int[][] pixelArray = ImageUtils.imageToColorArray(image);

        TwoDPosition topLeft = null;
        TwoDPosition bottomRight = null;
        boolean first = true;
        for(int i = 0; i < pixelArray.length; i++) {
            for (int j = 0; j < pixelArray[0].length; j++) {
                int currentPixel = pixelArray[i][j];
                if (currentPixel == pixelToFind) {
                    if (first) {
                        topLeft = new TwoDPosition(j, i);
                        first = false;
                    }
                    else {
                        bottomRight = new TwoDPosition(j, i);
                        break;
                    }
                }
            }
            if (bottomRight != null)
                break;
        }

        return new Area(topLeft, bottomRight);
    }

    @Override
    public String toString() {
        return topLeft.toString() + " > " + bottomRight.toString();
    }
}
