package arthur.silva.discordbot.base.infrastructure.application.utils;

import java.util.Collection;

public class StringUtils {

    public static String generateDisplayList(String separator, String lastSeparator, Collection<Object> objects) {
        return generateDisplayList(separator, lastSeparator, objects.toArray());
    }

    public static String generateDisplayList(String separator, String lastSeparator, Object... objects) {
        StringBuilder result = new StringBuilder();

        if (objects.length > 0) {
            result.append(objects[0]);
            for (int i = 1; i < objects.length - 1; i++) {
                result.append(separator).append(objects[i]);
            }

            if (objects.length > 1) {
                result.append(lastSeparator).append(objects[objects.length - 1]);
            }
        }
        return result.toString();
    }

    public static String getInvisibleCharacter() {
        return "\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00";
    }
}
