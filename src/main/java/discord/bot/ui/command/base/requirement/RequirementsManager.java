package discord.bot.ui.command.base.requirement;

import discord.bot.ui.events.MessageReceivedEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * Manages a command's requirements.
 */
@Scope("prototype")
@Component
public class RequirementsManager {
	private Set<Requirement> requirements = new HashSet<>();

	/**
	 * Registers a group of requirements.
	 *
	 * @param rest requirements to register
	 */
	public void setRequirements(Requirement first, @NonNull Requirement... rest) {
		requirements = EnumSet.of(first, rest);
	}

	/**
	 * @return This command's requirements.
	 */
	public Set<Requirement> getRequirements() {
		return EnumSet.copyOf(requirements);
	}

	/**
	 * Verifies if an event meets the requirements for this command's execution.
	 *
	 * @param event the event to verify
	 * @return the verification's result
	 */
	public RequirementVerificationResult meetsRequirements(@NonNull MessageReceivedEvent event) {
		for (Requirement requirement : requirements) {
			RequirementVerificationResult result = requirement.meetsRequirements(event);
			if (!result.successfulVerification)
				return result;
		}
		return new RequirementVerificationResult(true, null);
	}

	/**
	 * Verifies if the command has a certain requirement to be run.
	 *
	 * @param requirementsToVerify the requirements to verify
	 * @return if it has the requirement
	 */
	public boolean requires(@NonNull Requirement... requirementsToVerify) {
		for (Requirement requirementToVerify : requirementsToVerify) {
			if (!requirements.contains(requirementToVerify))
				return false;
		}
		return true;
	}

	/**
	 * Verifies if none of these requirements are necessary.
	 */
	public boolean doesNotRequire(@NonNull Requirement... requirementsToVerify) {
		for (Requirement requirementToVerify : requirementsToVerify) {
			if (requirements.contains(requirementToVerify))
				return false;
		}
		return true;
	}

	/**
	 * Verifies if the command has only certain requirements to be run.
	 *
	 * @param mustHaveRequirements The commands must have these requirements. Can be null
	 * @param mayAlsoHaveRequirements The commands may also have these requirements. Can be null
	 * @return If it has only the requirements provided.
	 */
	public boolean requiresOnly(@Nullable Requirement[] mustHaveRequirements, @Nullable Requirement[] mayAlsoHaveRequirements) {
		if (mustHaveRequirements == null) {
			mustHaveRequirements = new Requirement[0];
		}
		if (mayAlsoHaveRequirements == null) {
			mayAlsoHaveRequirements = new Requirement[0];
		}

		for (Requirement mustHaveRequirement : mustHaveRequirements) {
			if (!requires(mustHaveRequirement))
				return false;
		}

		int nRequirements = requirements.size();
		int nRequirementsMatched = mustHaveRequirements.length;
		for (Requirement mayAlsoHaveRequirement : mayAlsoHaveRequirements) {
			if (requires(mayAlsoHaveRequirement)) {
				nRequirementsMatched++;
			}
		}
		return nRequirements == nRequirementsMatched;
	}

	/**
	 * Verifies if the command has only certain mustHaveRequirements to be run.
	 *
	 * @param mustHaveRequirements The mustHaveRequirements to verify. Can be null (= verify if there's no requirements).
	 * @return If it has only the mustHaveRequirements provided an no other.
	 */
	public boolean requiresOnly(@Nullable Requirement[] mustHaveRequirements) {
		return requiresOnly(mustHaveRequirements, null);
	}

}
