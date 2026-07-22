package midpoint.midpointoptimizer;

public class ModConfig {
    public int fpsTarget = 60;
    public boolean lowEndPreset = false;
    public HudAnchor hudAnchor = HudAnchor.TOP_RIGHT;
    public HudTextColor hudTextColor = HudTextColor.WHITE;

    public void save() throws java.io.IOException {
        ConfigManager.saveConfig(this);
    }
}
