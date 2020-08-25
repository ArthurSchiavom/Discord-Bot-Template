package discord.bot.infrastructure.application.utils;

import discord.bot.infrastructure.image.Area;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class ImageUtils {
    /**
     *
     * <br>Note: This method achieves higher performance than when using getRGB().
     *
     * @param image
     * @return
     */
    public static int[][] imageToColorArray(BufferedImage image) {
        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int width = image.getWidth();
        final int height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        int[][] result = new int[height][width];
        if (hasAlphaChannel) {
            final int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel + 3 < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
                argb += ((int) pixels[pixel + 1] & 0xff); // blue
                argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        } else {
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel + 2 < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += -16777216; // 255 alpha
                argb += ((int) pixels[pixel] & 0xff); // blue
                argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        }

        return result;
    }

    /**
     * Draw a String centered in the middle of a Rectangle.
     *
     * @param g The Graphics instance.
     * @param text The String to draw.
     * @param rect The Rectangle to center the text in.
     */
    public static void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
        // Get the FontMetrics
        FontMetrics metrics = g.getFontMetrics(font);
        // Determine the X coordinate for the text
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        // Set the font
        g.setFont(font);
        // Draw the String
        g.drawString(text, x, y);
    }

    public static Font scaleFont(int width, Graphics g, Font font, String text) {
        while (g.getFontMetrics(font).stringWidth(text) > width) {
            font = font.deriveFont(font.getSize() - 1.0f);
        }

        return font;
    }

    public static void addText(BufferedImage imageSoFar, String text, String fontName, int fontStyle, double defaultScaleAccordingToAreaSize, Color color, int topLeftX, int topLeftY, int bottomRightX, int bottomRightY) {
        Graphics2D g = (Graphics2D) imageSoFar.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(color);

        int height = bottomRightY - topLeftY;
        int width = bottomRightX - topLeftX;
        Rectangle rectangle = new Rectangle(topLeftX, topLeftY,
                width,
                height);

        Font font = new Font(fontName, fontStyle, (int) (height * defaultScaleAccordingToAreaSize));
        font = scaleFont(width, g, font, text);

        drawCenteredString(g, text, rectangle, font);

        g.dispose();
    }

    public static void addText(BufferedImage imageSoFar, String text, String fontName, int fontStyle, double defaultScaleAccordingToAreaSize, Color colorHex, Area area) {
        addText(imageSoFar, text, fontName, fontStyle, defaultScaleAccordingToAreaSize, colorHex, area.topLeft.x, area.topLeft.y,
                area.bottomRight.x, area.bottomRight.y);
    }

    public static int colorToInt(Color color) {
        return ((color.getAlpha() << 24) & 0xFF000000) | color.getRGB();
    }
}
