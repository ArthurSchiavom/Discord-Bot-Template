package arthur.silva.discordbot.base.infrastructure.application.utils;

import net.dv8tion.jda.api.EmbedBuilder;

/**
 * Utils for Discord embed manipulation.
 */
public class EmbedUtils {
    /**
     * Adjust an embed with only inline fields to have X columns (so it displays properly on PC)
     *
     * @param eb      The embed builder with only inline fields.
     * @param nFields The number of columns it should have.
     */
    public static void adjustEmbedInlineFields(EmbedBuilder eb, int nFields) {
        int nEmbedFields = eb.getFields().size();
        while (nEmbedFields % nFields != 0) {
            eb.addField("", "", true);
            nEmbedFields++;
        }
    }
}
