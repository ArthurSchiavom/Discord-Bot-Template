package discord.bot.data.database;

import discord.bot.domain.Placeholder_TEMP;
import org.springframework.data.repository.CrudRepository;

public interface PlaceholderRepo_TEMP extends CrudRepository<Placeholder_TEMP, String> {
    Placeholder_TEMP findByWhatever(String whatever);
}
