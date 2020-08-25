package discord.bot.data.card;

import discord.bot.core.card.Frame;
import discord.bot.data.configuration.GlobalConfiguration;
import lombok.Getter;
import org.springframework.context.annotation.DependsOn;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
@DependsOn("jdaBootstrapper")
public class FrameStorage {
    @Getter private final HashMap<String, Frame> frames = new LinkedHashMap<>();
    @Getter private final HashMap<String, Frame> defaultFrames = new LinkedHashMap<>();

    public void addFrame(@NonNull Frame frame) {
        frames.put(frame.frameName, frame);
    }

    public void addDefaultFrame(@NonNull Frame frame) {
        defaultFrames.put(frame.frameName, frame);
    }
}
