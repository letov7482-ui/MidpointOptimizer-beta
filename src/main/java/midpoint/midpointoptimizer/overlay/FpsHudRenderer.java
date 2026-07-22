package midpoint.midpointoptimizer.Overlay;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class FpsHudRenderer {
    private final ModConfig config;
    private final SmartOptimizerController controller;

    public FpsHudRenderer(ModConfig config, SmartOptimizerController controller) {
        this.config = config;
        this.controller = controller;
    }

    public void render(DrawContext context, MinecraftClient client) {
        // Рендер HUD с адаптивным FPS (как в оригинале)
        context.drawTextWithShadow(client.textRenderer, "Midpoint FPS: " + client.getCurrentFps(), 10, 10, 0xFFFFFF);
        // Здесь будет весь твой оригинальный HUD + коллеры
    }
}
