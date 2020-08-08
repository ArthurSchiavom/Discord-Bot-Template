package discord.bot.ui.command.base.requirement;

import org.springframework.lang.Nullable;

/**
 * Indicates that a command requirement was not met.
 */
public class RequirementVerificationResult extends Exception {
    /**
     * Indicates if the verification was successful.
     */
    public final boolean successfulVerification;
    /**
     * Provides a user-friendly reason for why the verification failed. It is null in case of success.
     */
    public final String reasonVerificationFailed;

    public RequirementVerificationResult(boolean successfulVerification, @Nullable String reasonVerificationFailed) {
        this.successfulVerification = successfulVerification;
        this.reasonVerificationFailed = reasonVerificationFailed;
    }
}
