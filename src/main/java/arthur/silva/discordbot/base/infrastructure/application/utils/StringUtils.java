package arthur.silva.discordbot.base.infrastructure.application.utils;

import org.springframework.lang.NonNull;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;

/**
 * Utilities for String manipulation.
 */
public class StringUtils {

    /**
     * Generates a list in String format.
     *
     * @param separator string that separates each list element
     * @param lastSeparator string that separates the last elements of the list
     * @param objects elements
     * @return generated String list
     */
    public static String generateDisplayList(@NonNull String separator, @NonNull String lastSeparator, @NonNull Collection<?> objects) {
        return generateDisplayList(separator, lastSeparator, objects.toArray(new Object[0]));
    }

    /**
     * Generates a list in String format.
     *
     * @param separator string that separates each list element
     * @param lastSeparator string that separates the last elements of the list
     * @param objects elements
     * @return generated String list
     */
    public static String generateDisplayList(@NonNull String separator, @NonNull String lastSeparator, @NonNull Object... objects) {
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

    /**
     * @return a character that's invisible in Discord
     */
    public static String getInvisibleCharacter() {
        return "\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00\uDB40\uDC00";
    }

    /**
     * Joins a group of String array elements with a given separator.
     *
     * @param firstIndex index of the first element to join
     * @param lastIndex index of the last element to join
     * @param separator string that separates each element
     * @param lastSeparator string that separates the last 2 elements that are joined
     * @param values the array to process
     * @return the String with joined elements
     */
    public static String joinElements(int firstIndex, int lastIndex, String separator, String lastSeparator, String[] values) {
        StringBuilder sb = new StringBuilder();

        if (firstIndex <= lastIndex)
            sb.append(values[firstIndex]);

        for (int i = firstIndex + 1; i < lastIndex; i++) {
            sb.append(separator).append(values[i]);
        }

        if (firstIndex != lastIndex)
            sb.append(lastSeparator).append(values[lastIndex]);

        return sb.toString();
    }

    /**
     * Converts a number into a string of Discord emojis.
     *
     * @param number the number to convert
     * @return the conversion result
     */
    public static String convertNumberToEmojiDisplay(long number) {
        String numberString = Long.toString(number);
        return replaceMathCharactersWithEmojis(numberString);
    }

    /**
     * Replaces a message's numbers, +, -, /, x and * with Discord emojis.
     *
     * @param str the string the process
     * @return the processed string
     */
    public static String replaceMathCharactersWithEmojis(String str) {
        return str
                .replace("-", ":heavy_minus_sign:")
                .replace("+", ":heavy_plus_sign:")
                .replace("x", ":heavy_multiplication_x:")
                .replace("*", ":heavy_multiplication_x:")
                .replace("/", ":heavy_division_sign:")
                .replace("0", ":zero:")
                .replace("1", ":one:")
                .replace("2", ":two:")
                .replace("3", ":three:")
                .replace("4", ":four:")
                .replace("5", ":five:")
                .replace("6", ":six:")
                .replace("7", ":seven:")
                .replace("8", ":eight:")
                .replace("9", ":nine:");
    }

    /**
     * Converts a calendar information to full day, month and year format.
     * <br>Check <a href="https://docs.oracle.com/javase/8/docs/api/java/util/Formatter.html#dt">Formatter docs</a> for more info.
     *
     * @param cal The target calendar.
     * @return A full textual representation of the day, month and year.
     */
    public static String calendarToDateDisplay(Calendar cal) {
        return String.format(Locale.ENGLISH
                , "%te of %tB, %tY"
                , cal
                , cal
                , cal);
    }

    /**
     * Converts a calendar information to 24H time format (HH:MMM).
     * <br>Check <a href="https://docs.oracle.com/javase/8/docs/api/java/util/Formatter.html#dt">Formatter docs</a> for more info.
     *
     * @param cal The target calendar.
     * @return A textual representation of the time as HH:MM.
     */
    public static String calendarToTimeDisplay(Calendar cal) {
        return String.format("%tR"
                , cal);
    }

    /**
     * Converts a calendar information to full day, month and year format + 24H time format as HH:MM.
     * <br>Check <a href="https://docs.oracle.com/javase/8/docs/api/java/util/Formatter.html#dt">Formatter docs</a> for more info.
     *
     * @param cal The target calendar.
     * @return A full textual representation of the day, month and year + 24H time as HH:MM.
     */
    public static String calendarToDateTimeDisplay(Calendar cal) {
        return calendarToDateDisplay(cal) + " - " + calendarToTimeDisplay(cal);
    }
}
