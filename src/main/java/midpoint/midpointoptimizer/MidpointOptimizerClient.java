package midpoint.midpointoptimizer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.MinecraftClient;

public class MidpointOptimizerClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Оптимизация FPS + SmartOptimizer (из оригинала)
            // Здесь будет твой луп каждые 10 тиков
        });
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            // Сохранение конфига при выходе
            try {
                ConfigManager.saveConfig(ModConfig.loadConfig());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
