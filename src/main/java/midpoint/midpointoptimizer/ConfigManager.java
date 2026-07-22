package midpoint.midpointoptimizer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

public class ConfigManager {
    private static final File CONFIG_FILE = new File("config/midpoint.properties");

    public static void saveConfig(ModConfig config) throws IOException {
        Properties props = new Properties();
        props.setProperty("fpsTarget", String.valueOf(config.fpsTarget));
        props.setProperty("lowEndPreset", String.valueOf(config.lowEndPreset));
        props.setProperty("hudAnchor", config.hudAnchor.name());
        props.setProperty("hudTextColor", config.hudTextColor.name());
        Files.createDirectories(CONFIG_FILE.getParentFile().toPath());
        Files.write(CONFIG_FILE.toPath(), props.storeToStream(null, null).getBytes());
    }

    public static ModConfig loadConfig() {
        ModConfig config = new ModConfig();
        if (CONFIG_FILE.exists()) {
            try (var in = Files.newBufferedReader(CONFIG_FILE.toPath())) {
                Properties props = new Properties();
                props.load(in);
                config.fpsTarget = Integer.parseInt(props.getProperty("fpsTarget", "60"));
                config.lowEndPreset = Boolean.parseBoolean(props.getProperty("lowEndPreset", "false"));
                config.hudAnchor = HudAnchor.valueOf(props.getProperty("hudAnchor", "TOP_RIGHT"));
                config.hudTextColor = HudTextColor.valueOf(props.getProperty("hudTextColor", "WHITE"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return config;
    }
}
