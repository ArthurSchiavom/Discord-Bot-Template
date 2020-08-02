package arthur.silva.discordbot.base.ui.command.base;

import lombok.Getter;

/**
 * Command category.
 */
public enum Category implements Comparable<Category> {
	MODERATION("\uD83D\uDD28 Moderation")
	, INFORMATION("❓ Information")
	, FUN("\uD83D\uDE04 Fun")
	, UTILITY("\uD83D\uDD27 Utility")
	, CONFIGURATION("⚙ Configuration")
	, MUSIC("\uD83C\uDFB6 Music");

	@Getter private final String name;
	Category(String name) {
		this.name = name;
	}
}
