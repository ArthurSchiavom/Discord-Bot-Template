package arthur.silva.discordbot.base.infrastructure.application.utils;

import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CollectionUtils {
    public static boolean doCollectionsOverlap (@NonNull Collection<?>... collections) {
        Set<Object> set = new HashSet<>();
        int expectedSize = 0;
        for (Collection<?> collection : collections) {
            set.addAll(collection);
            expectedSize += collection.size();
        }

        return set.size() != expectedSize;
    }
}
