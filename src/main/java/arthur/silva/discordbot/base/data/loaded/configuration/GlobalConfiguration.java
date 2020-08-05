package arthur.silva.discordbot.base.data.loaded.configuration;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import org.springframework.lang.NonNull;

import java.awt.*;

public class GlobalConfiguration {
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

        public static void configHelpEmbedFooter(EmbedBuilder eb) {
            if (helpEmbedFooterImageUrl != null && helpEmbedFooterText != null)
                eb.setFooter(helpEmbedFooterText, helpEmbedFooterImageUrl);
            else if (helpEmbedFooterText != null)
                eb.setFooter(helpEmbedFooterText);
        }

        public static void configEmbedThumbnail(EmbedBuilder eb) {
            if (mainMenuThumbnail != null)
                eb.setThumbnail(Command.mainMenuThumbnail);
        }
    }

    /**
     * Bot configurations.
     */
    public static class Bot {
        public static String token = null;
        public static JDA jda = null;

        /**
         * Defaults to null
         */
        public static String playingStatus = null;
    }
}
