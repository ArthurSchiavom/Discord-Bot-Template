package discord.bot.data.card;

import discord.bot.core.card.Character;
import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class CharacterStorage {
    @Getter private final Set<Character> characters = new LinkedHashSet<>();

    public void addCharacter(@NonNull Character character) {
        characters.add(character);
    }
}
