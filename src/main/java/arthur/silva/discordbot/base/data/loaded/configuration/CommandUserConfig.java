package arthur.silva.discordbot.base.data.loaded.configuration;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class CommandUserConfig {
    public static String commandPrefix;
    public static int commandPrefixNChars;
    /** Defaults to #28CF75 */
    public static Color defaultEmbedColor = Color.decode("#28CF75");
    /** Defaults to null */
    public static String helpEmbedFooterImageUrl = null;
    /** Defaults to null */
    public static String helpEmbedFooterText = null;
    /** Defaults to null */
    public static String mainMenuThumbnail = null;

    public static void configHelpEmbedFooter(EmbedBuilder eb) {
        if (helpEmbedFooterImageUrl != null && helpEmbedFooterText != null)
            eb.setFooter(helpEmbedFooterText, helpEmbedFooterImageUrl);
        else if (helpEmbedFooterText != null)
            eb.setFooter(helpEmbedFooterText);
    }
}
