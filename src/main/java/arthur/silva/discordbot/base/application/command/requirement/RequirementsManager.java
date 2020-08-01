package arthur.silva.discordbot.base.application.command.requirement;

import arthur.silva.discordbot.base.application.events.MessageReceivedEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages a command's requirements.
 */
@Scope("prototype")
@Component
public class RequirementsManager {
	private final List<Requirement> requirements = new ArrayList<>();

	/**
	 * Registers a group of requirements.
	 *
	 * @param requirementsToRegister requirements to register
	 */
	public void registerRequirements(@NonNull Requirement... requirementsToRegister) {
		for (Requirement requirementToAdd : requirementsToRegister) {
			registerRequirement(requirementToAdd);
		}
	}

	/**
	 * Registers a requirement.
	 *
	 * @param requirement requirement to register
	 */
	public void registerRequirement(@NonNull Requirement requirement) {
		if (!requirements.contains(requirement))
			requirements.add(requirement);
	}

	/**
	 * @return This command's requirements.
	 */
	public List<Requirement> getRequirements() {
		return new ArrayList<>(requirements);
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
	 * @param requirement The requirement to verify.
	 * @return If it has the requirement.
	 */
	public boolean requires(@NonNull Requirement requirement) {
		return requirements.contains(requirement);
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
