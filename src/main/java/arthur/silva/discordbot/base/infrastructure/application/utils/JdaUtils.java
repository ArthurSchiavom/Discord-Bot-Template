package arthur.silva.discordbot.base.infrastructure.application.utils;

import arthur.silva.discordbot.base.application.events.MessageReceivedEvent;
import arthur.silva.discordbot.base.infrastructure.application.utils.exception.JDANotConnectedException;
import arthur.silva.discordbot.base.infrastructure.application.utils.exception.MessageUnavailableException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.requests.ErrorResponse;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Utils for JDA usage.
 */
public class JdaUtils {

    /**
     * Attempts to retrieve a message if it is available.
     *
     * @param jda the JDA instance to use
     * @param guildId the ID of the guild where the message is
     * @param channelId the ID of the channel where the message is
     * @param msgId the ID of the message
     * @return the message retrieved
     * @throws MessageUnavailableException if the message wasn't available for the bot account to access
     * @throws JDANotConnectedException if JDA is not connected to Discord
     */
    public static Message retrieveMessageIfAvailable(JDA jda, String guildId, String channelId, String msgId) throws MessageUnavailableException, JDANotConnectedException {
        try {
            return jda.getGuildById(guildId).getTextChannelById(channelId).retrieveMessageById(msgId).complete();
        } catch (Exception e) {
            if (e instanceof ErrorResponseException) {
                ErrorResponse errorResponse = ((ErrorResponseException) e).getErrorResponse();
                switch (errorResponse) {
                    case MISSING_ACCESS:
                    case MISSING_PERMISSIONS:
                    case UNKNOWN_MESSAGE:
                    case UNKNOWN_CHANNEL:
                        throw new MessageUnavailableException("The message isn't available. (No permission to access or deleted)");
                }
            }
            throw new JDANotConnectedException("The bot isn't connected to Discord.");
        }
    }

    /**
     * Message all users with a certain role.
     *
     * @param guild            the target guild
     * @param role             the role
     * @param message          the message to send
     * @param exceptionUserIds the users to not message
     */
    public static void messageAllUsersWithRole(Guild guild, Role role, Message message, List<Long> exceptionUserIds) {
        List<Member> membersWithRole = guild.getMembersWithRoles(role);
        if (exceptionUserIds == null)
            exceptionUserIds = new ArrayList<>();

        for (Member member : membersWithRole) {
            long userId = member.getUser().getIdLong();
            if (!exceptionUserIds.contains(userId)) {
                member.getUser().openPrivateChannel().queue(channel -> {
                    channel.sendMessage(message).queue(null, e -> {/* Override println error message with nothing as this means that the user doesn't allow DMs */});
                });
            }
        }
    }

    /**
     * Verifies if a member has a certain role.
     *
     * @param member the member to verify
     * @param roleId the ID of the role
     * @return if the member has the role
     */
    public static boolean doesMemberHaveRole(Member member, long roleId) {
        for (Role role : member.getRoles()) {
            if (role.getIdLong() == roleId)
                return true;
        }
        return false;
    }

    /**
     * Verifies if a member has a certain role.
     *
     * @param member the member to verify
     * @param roleId the ID of the role
     * @return if the member has the role
     */
    public static boolean doesMemberHaveRole(Member member, String roleId) {
        long idLong = Long.parseLong(roleId);
        return doesMemberHaveRole(member, idLong);
    }

    /**
     * Get the first mentioned channel in a message.
     *
     * @param msg the message to analyze
     * @return (1) the first channel mentioned if any or (2) null if no channel mentioned
     */
    public static MessageChannel retrieveSingleMentionedChannel(Message msg) {
        List<TextChannel> mentionedChannels = msg.getMentionedChannels();
        if (mentionedChannels.size() < 1)
            return null;
        else
            return mentionedChannels.get(0);
    }

    /**
     * Get the first mentioned role in an event.
     *
     * @param event the event to analyze
     * @return (1) the first role mentioned if any or (2) null if no role mentioned
     */
    public static Role retrieveRoleMentioned(MessageReceivedEvent event) {
        List<Role> rolesMentioned = event.getMessage().getMentionedRoles();
        if (rolesMentioned.size() < 1) {
            return null;
        } else
            return rolesMentioned.get(0);
    }

    /**
     * Retrieves the IDs of the roles that a member has.
     *
     * @param member the member to analyse
     * @return the IDs of the roles that the member has
     */
    public static List<Long> retrieveMemberRolesIds(Member member) {
        List<Long> rolesIds = new ArrayList<>();
        for (Role role : member.getRoles()) {
            rolesIds.add(role.getIdLong());
        }
        return rolesIds;
    }

}
