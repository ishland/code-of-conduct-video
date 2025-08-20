package com.ishland.code_of_conduct_video;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class TheMod implements ModInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger("Code of Conduct Video");

    public static String[] videoFrames = new String[0];

    @Override
    public void onInitialize() {
        try {
            Path framesDir = Path.of(".", "code_of_conduct_video_frames");
            if (!Files.isDirectory(framesDir)) {
                Files.createDirectories(framesDir);
            }
            List<Path> list = Files.list(framesDir).filter(path -> path.getFileName().toString().endsWith(".txt")).sorted().toList();
            videoFrames = new String[list.size()];
            Arrays.fill(videoFrames, "");
            for (int i = 0; i < list.size(); i++) {
                Path path = list.get(i);
                try {
                    String content = Files.readString(path);
                    videoFrames[i] = content;
                } catch (Throwable t) {
                    LOGGER.error("Failed to read frame file: {}", path, t);
                    videoFrames[i] = "";
                }
            }
            LOGGER.info("Loaded {} frames from {}", videoFrames.length, framesDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
