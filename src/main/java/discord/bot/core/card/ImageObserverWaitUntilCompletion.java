package discord.bot.core.card;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class ImageObserverWaitUntilCompletion implements ImageObserver {

    private final AtomicBoolean finished = new AtomicBoolean();

    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        System.out.println("a");
        synchronized (this) {
            finished.set(true);
            notify();
            return false;
        }
    }

    public void waitUntilCompletion() {
        synchronized (this) {
            if (!finished.get()) {
                try {
                    wait(1000);
                } catch (InterruptedException e) {
                    log.error("Failed to wait for the image rendering to finish: ");
                    e.printStackTrace();
                }
            }
        }
    }
}
