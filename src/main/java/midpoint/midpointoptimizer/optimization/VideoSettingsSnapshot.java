package midpoint.midpointoptimizer.optimization;

import net.minecraft.client.option.VideoOptions;

public class VideoSettingsSnapshot {
    private final VideoOptions options;
    public VideoSettingsSnapshot(VideoOptions options) {
        this.options = options;
    }
    // Здесь будет сохранение/восстановление настроек для безопасной оптимизации
}
