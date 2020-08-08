package arthur.silva.discordbot.base.data.repository;

import arthur.silva.discordbot.base.domain.Placeholder_TEMP;
import org.springframework.data.repository.CrudRepository;

public interface PlaceholderRepo_TEMP extends CrudRepository<Placeholder_TEMP, String> {
    Placeholder_TEMP findByWhatever(String whatever);
}
