package arthur.silva.discordbot.base.ui.events;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

/**
 * Represents a generic message event.
 */
public class MessageReceivedEvent {
	private final MessageOrigin messageOrigin;
	private final GuildMessageReceivedEvent guildMessageEvent;
	private final PrivateMessageReceivedEvent privateMessageEvent;

	// Both
	private final User author;
	private final Message message;
	private final MessageChannel channel;
	private final JDA jda;

	// GuildInfo only
	private final TextChannel guildChannel;
	private final Guild guild;
	private final Member member;

	// Private only
	private PrivateChannel privateChannel = null;

	/**
	 * Creates a MessageReceivedEvent representing a GuildMessageReceivedEvent.
	 * @param guildMessageEvent The event it should represent.
	 */
	public MessageReceivedEvent(GuildMessageReceivedEvent guildMessageEvent) {
		this.guildMessageEvent = guildMessageEvent;
		this.privateMessageEvent = null;
		author = guildMessageEvent.getAuthor();
		message = guildMessageEvent.getMessage();
		guildChannel = guildMessageEvent.getChannel();
		channel = guildChannel;
		jda = guildMessageEvent.getJDA();
		guild = guildMessageEvent.getGuild();
		member = guildMessageEvent.getMember();
		messageOrigin = MessageOrigin.GUILD;
	}

	/**
	 * Creates a MessageReceivedEvent representing a PrivateMessageReceivedEvent.
	 * @param privateMessageEvent The event it should represent.
	 */
	public MessageReceivedEvent(PrivateMessageReceivedEvent privateMessageEvent) {
		this.privateMessageEvent = privateMessageEvent;
		this.guildMessageEvent = null;
		author = privateMessageEvent.getAuthor();
		message = privateMessageEvent.getMessage();
		privateChannel = privateMessageEvent.getChannel();
		channel = privateMessageEvent.getChannel();
		jda = privateMessageEvent.getJDA();
		messageOrigin = MessageOrigin.PRIVATE_CHANNEL;
		guildChannel = null;
		guild = null;
		member = null;
	}

	/**
	 * @return The event's author.
	 */
	public User getAuthor() {
		return author;
	}

	/**
	 * @return The message transmitted by the event.
	 */
	public Message getMessage() {
		return message;
	}

	/**
	 * @return The channel where the event originated.
	 */
	public MessageChannel getChannel() {
		return channel;
	}

	/**
	 * @return The JDA instance that received the event.
	 */
	public JDA getJda() {
		return jda;
	}

	/**
	 * @return The guild channel where the message was sent or
	 * <br>null if this event did not originate in a guild.
	 */
	public TextChannel getGuildChannel() {
		return guildChannel;
	}

	/**
	 * @return The guild where the message was sent or
	 * <br>null if this event did not originate in a guild.
	 */
	public Guild getGuild() {
		return guild;
	}

	/**
	 *
	 * @return The member that sent the message or
	 * <br>null if this event did not originate in a guild.
	 */
	public Member getMember() {
		return member;
	}

	/**
	 * @return The private channel where the message was sent or
	 * <br>null if this event did not originate in a private channel.
	 */
	public PrivateChannel getPrivateChannel() {
		return privateChannel;
	}

	/**
	 * @return This message's type.
	 */
	public MessageOrigin getMessageOrigin() {
		return messageOrigin;
	}

	public GuildMessageReceivedEvent getGuildMessageEvent() {
		return guildMessageEvent;
	}

	public PrivateMessageReceivedEvent getPrivateMessageEvent() {
		return privateMessageEvent;
	}
}
