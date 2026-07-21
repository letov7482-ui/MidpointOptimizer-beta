package com.midpointoptimizer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import com.midpointoptimizer.config.ModConfig;
import com.midpointoptimizer.hud.FPSHudRenderer;
import com.midpointoptimizer.optimizer.SmartOptimizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MidpointOptimizer implements ClientModInitializer {
    public static final String MOD_ID = "midpoint-optimizer";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    private static MidpointOptimizer instance;
    private ModConfig config;
    private FPSHudRenderer hudRenderer;
    private SmartOptimizer optimizer;

    @Override
    public void onInitializeClient() {
        instance = this;
        LOGGER.info("MidpointOptimizer v1.0.0 initializing for Fabric 1.21.11");
        
        // Load configuration
        config = ModConfig.load();
        
        // Initialize FPS HUD
        hudRenderer = new FPSHudRenderer(config);
        HudRenderCallback.EVENT.register(hudRenderer);
        
        // Initialize Smart Optimizer
        optimizer = new SmartOptimizer(config);
        ClientTickEvents.END_CLIENT_TICK.register(optimizer);
        
        LOGGER.info("MidpointOptimizer initialized successfully!");
    }
    
    public static MidpointOptimizer getInstance() {
        return instance;
    }
    
    public ModConfig getConfig() {
        return config;
    }
}
