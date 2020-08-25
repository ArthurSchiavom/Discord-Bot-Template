package discord.bot.data.configuration;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;

import java.awt.*;

/**
 * Holds global application configurations.
 */
public class GlobalConfiguration {

    /**
     * Configurations related to commands.
     */
    public static class Command {
        public static final String ARGUMENT_SEPARATOR = " "; // Used to separate parent/child commands

        public static String commandPrefix;
        public static int commandPrefixNChars;
        /**
         * Defaults to #28CF75
         */
        public static Color defaultEmbedColor = Color.decode("#28CF75");
        /**
         * Defaults to null
         */
        public static String helpEmbedFooterImageUrl = null;
        /**
         * Defaults to null
         */
        public static String helpEmbedFooterText = null;
        /**
         * Defaults to null
         */
        public static String mainMenuThumbnail = null;

        /**
         * Sets the embed's footer to match the configuration values.
         */
        public static void configHelpEmbedFooter(EmbedBuilder eb) {
            if (helpEmbedFooterImageUrl != null && helpEmbedFooterText != null)
                eb.setFooter(helpEmbedFooterText, helpEmbedFooterImageUrl);
            else if (helpEmbedFooterText != null)
                eb.setFooter(helpEmbedFooterText);
        }

        /**
         * Sets the embed's thumbnail to match the configuration values.
         */
        public static void configEmbedThumbnail(EmbedBuilder eb) {
            if (mainMenuThumbnail != null)
                eb.setThumbnail(Command.mainMenuThumbnail);
        }
    }

    /**
     * Configurations related to Discord.
     */
    public static class Bot {
        public static String token = null;
        public static JDA jda = null;

        /**
         * Defaults to null
         */
        public static String playingStatus = null;
    }

    public static class Font {
        public static final String FONT_DIRECTORY = "assets/fonts";
    }

    public static class Card {
        public static final String CHARACTER_IMAGE_DIRECTORY = "assets/characters";
        public static final String FRAME_IMAGE_DIRECTORY = "assets/frames";
        public static final String DEFAULT_FRAMES_IMAGE_DIRECTORY = "assets/default-frames";

        public static Color characterNameAreaDelimiterColor = null;
        public static Color animeNameAreaDelimiterColor = null;
        public static Color characterImageAreaDelimiterColor = null;
        public static Color animeNameColor = null;
        public static String animeNameFont = null;
        public static Color characterNameColor = null;
        public static String characterNameFont = null;
    }
}
