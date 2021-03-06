package discord.bot.ui.events.operators;

/**
 * Processes an event.
 *
 * @param <T> the type of events it can process
 */
public interface EventOperator<T> {
    /**
     * Processes an event.
     *
     * @param t the event to process
     */
    void processMessageReceived(T t);
}
