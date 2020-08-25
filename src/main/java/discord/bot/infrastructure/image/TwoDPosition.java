package discord.bot.infrastructure.image;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class TwoDPosition {
    public final int x;
    public final int y;

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
