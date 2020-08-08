package discord.bot.infrastructure.application.utils;

import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides utilities for Collections usage
 */
public class CollectionUtils {
    /**
     * Verifies if there are overlapping elements in a collection.
     *
     * @param collections the collections to analyse.
     * @return (1) true if there are overlapping elements or (2) false otherwise
     */
    public static boolean doCollectionsOverlap (@NonNull Collection<?>... collections) {
        return CountOverlappingElements(collections) != 0;
    }

    /**
     * Counts the number of overlapping elements in a collection.
     *
     * @param collections the collections to analyse.
     * @return the number of overlapping elements (non-unique)
     */
    public static int CountOverlappingElements(@NonNull Collection<?>... collections) {
        Set<Object> set = new HashSet<>();
        int expectedSize = 0;
        for (Collection<?> collection : collections) {
            set.addAll(collection);
            expectedSize += collection.size();
        }

        return expectedSize - set.size();
    }

}
