package com.midpointoptimizer.optimizer;

import com.midpointoptimizer.config.ModConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.ParticlesMode;
import net.minecraft.client.option.CloudRenderMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmartOptimizer implements ClientTickEvents.EndTick {
    private static final Logger LOGGER = LoggerFactory.getLogger(SmartOptimizer.class);
    private final ModConfig config;
    private int tickCounter = 0;
    private boolean isLowFps = false;
    private int previousRenderDistance = -1;
    
    public SmartOptimizer(ModConfig config) {
        this.config = config;
    }
    
    @Override
    public void onEndTick(MinecraftClient client) {
        if (client == null || client.world == null) return;
        
        tickCounter++;
        if (tickCounter % 20 != 0) return;
        
        // === Smart Optimizer ===
        if (config.smartOptimizerEnabled) {
            int currentFps = getCurrentFps();
            boolean fpsDropped = currentFps < config.targetFps && currentFps > 0;
            
            if (fpsDropped && !isLowFps) {
                isLowFps = true;
                applyOptimizations(client);
                LOGGER.info("FPS dropped to {}, applying optimizations", currentFps);
            } else if (!fpsDropped && isLowFps) {
                isLowFps = false;
                revertOptimizations(client);
                LOGGER.info("FPS restored, reverting optimizations");
            }
        }
        
        // === Low End Mode ===
        if (config.lowEndMode) {
            applyLowEndOptimizations(client);
        }
    }
    
    private void applyOptimizations(MinecraftClient client) {
        if (config.optimizeRender) {
            int currentDist = client.options.getClampedViewDistance().getValue();
            if (previousRenderDistance == -1) {
                previousRenderDistance = currentDist;
            }
            client.options.getClampedViewDistance().setValue(Math.min(currentDist, 8));
        }
        
        if (config.optimizeParticles) {
            client.options.getParticles().setValue(ParticlesMode.MINIMAL);
        }
        
        if (config.optimizeChunks) {
            client.worldRenderer.scheduleTerrainUpdate();
        }
        
        if (config.optimizeEntities) {
            client.worldRenderer.setBlockBreakingInfo(0, null);
        }
        
        if (config.optimizeAnimations) {
            // Reduce animation speed
        }
        
        if (config.reduceRendering) {
            client.options.getBobView().setValue(false);
            client.options.getFovEffectScale().setValue(0.0);
        }
    }
    
    private void revertOptimizations(MinecraftClient client) {
        if (config.optimizeRender && previousRenderDistance != -1) {
            client.options.getClampedViewDistance().setValue(previousRenderDistance);
            previousRenderDistance = -1;
        }
        
        if (config.optimizeParticles) {
            client.options.getParticles().setValue(ParticlesMode.ALL);
        }
        
        if (config.reduceRendering) {
            client.options.getBobView().setValue(true);
            client.options.getFovEffectScale().setValue(1.0);
        }
    }
    
    private void applyLowEndOptimizations(MinecraftClient client) {
        if (config.reduceParticles) {
            client.options.getParticles().setValue(ParticlesMode.MINIMAL);
        }
        
        if (config.optimizeClouds) {
            client.options.getCloudRenderMode().setValue(CloudRenderMode.OFF);
        }
        
        if (config.optimizeTransparency) {
            client.options.getGraphicsMode().setValue(net.minecraft.client.option.GraphicsMode.FABULOUS);
        }
        
        if (config.optimizeEntityRendering) {
            client.options.getEntityShadows().setValue(false);
        }
        
        if (config.optimizeChunkUpdates) {
            client.worldRenderer.scheduleTerrainUpdate();
        }
        
        if (config.disableExpensiveEffects) {
            client.options.getCloudRenderMode().setValue(CloudRenderMode.OFF);
            client.options.getParticles().setValue(ParticlesMode.MINIMAL);
            client.options.getEntityShadows().setValue(false);
        }
    }
    
    private int getCurrentFps() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return 60;
        
        long currentTime = System.currentTimeMillis();
        return 60;
    }
}
