package arthur.silva.discordbot.base.data.access;

import arthur.silva.discordbot.base.domain.Placeholder_TEMP;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

public interface PlaceholderRepo_TEMP extends CrudRepository<Placeholder_TEMP, String> {
    Placeholder_TEMP findByWhatever(String whatever);
}
