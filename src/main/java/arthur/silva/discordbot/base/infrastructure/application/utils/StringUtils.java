package arthur.silva.discordbot.base.infrastructure.application.utils;

import org.springframework.lang.NonNull;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class StringUtils {

    public static final String EMPTY_LIST_TEXT_CONVERSION_MESSAGE = "Empty list";

    public static String generateDisplayList(String separator, String lastSeparator, Collection<?> objects) {
        return generateDisplayList(separator, lastSeparator, objects.toArray(new Object[0]));
    }

    public static String generateDisplayList(String separator, String lastSeparator, Object... objects) {
        return generateDisplayList(separator, lastSeparator, null, null, objects);
    }

    /**
     * Calculates a visual representation of a list of strings: String1, String2, String3,...
     *
     * @return A visual representation of the list of strings if any or
     * <br>EMPTY_LIST_TEXT_CONVERSION_MESSAGE constant of this class if the list or null or empty.
     */
    public static String generateDisplayList(@NonNull String separator, @NonNull String lastSeparator, @Nullable String beforeElement, @Nullable String afterElement, @NonNull Object... objects) {
        if (objects.length == 0)
            return "";

        if (beforeElement == null)
            beforeElement = "";
        if (afterElement == null)
            afterElement = "";

        StringBuilder result = new StringBuilder();

        result.append(beforeElement).append(objects[0]).append(afterElement);
        for (int i = 1; i < objects.length - 1; i++) {
            result.append(separator).append(beforeElement).append(objects[i]).append(afterElement);
        }

        if (objects.length > 1) {
            result.append(lastSeparator).append(beforeElement).append(objects[objects.length - 1]).append(afterElement);
        }

        return result.toString();
    }

    public static String getInvisibleCharacter() {
        return "\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00";
    }

    public static String joinElements(int firstIndex, int lastIndex, String separator, String[] values) {
        StringBuilder sb = new StringBuilder();

        if (firstIndex <= lastIndex)
            sb.append(values[firstIndex]);

        for (int i = firstIndex + 1; i <= lastIndex; i++) {
            sb.append(separator).append(values[i]);
        }

        return sb.toString();
    }
}
