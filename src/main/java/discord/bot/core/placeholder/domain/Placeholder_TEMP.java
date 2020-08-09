package discord.bot.core.placeholder.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Placeholder_TEMP {
    @Id
    public String id;
    public int whatever;

    public Placeholder_TEMP() {
    }

    public Placeholder_TEMP(String id) {
        this.id = id;
    }
}
