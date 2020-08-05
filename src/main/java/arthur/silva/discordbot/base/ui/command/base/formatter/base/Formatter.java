package arthur.silva.discordbot.base.ui.command.base.formatter.base;

import net.dv8tion.jda.api.entities.Message;

/**
 * Formats an accepted object into a user message.
 *
 * @param <T> the accepted type to format
 */
public interface Formatter<T> {
    /**
     * Format an object.
     *
     * @param t target to format
     * @return the formatted message
     */
    public abstract Message format(T t);
}
