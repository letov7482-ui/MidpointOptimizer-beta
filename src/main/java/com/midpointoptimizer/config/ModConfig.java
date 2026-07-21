package com.midpointoptimizer.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.midpointoptimizer.MidpointOptimizer;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModConfig {
    // === FPS Indicator Settings ===
    public boolean fpsEnabled = true;
    public boolean showCurrentFps = true;
    public boolean showAverageFps = true;
    public boolean showFrameTime = true;
    public boolean showMemoryUsage = true;
    public boolean showCpuUsage = false;
    public int hudX = 10;
    public int hudY = 10;
    public float hudScale = 1.0f;
    public boolean showBackground = true;
    public int backgroundAlpha = 150;
    public boolean showShadow = true;
    public int textColor = 0xFFFFFF;
    
    // === Smart Optimizer Settings ===
    public boolean smartOptimizerEnabled = true;
    public int targetFps = 60;
    public boolean optimizeRender = true;
    public boolean optimizeChunks = true;
    public boolean optimizeEntities = true;
    public boolean optimizeParticles = true;
    public boolean optimizeAnimations = true;
    public boolean cacheCalculations = true;
    public boolean reduceRendering = true;
    public boolean reduceAllocations = true;
    public boolean lowerGcPressure = true;
    public boolean improveFramePacing = true;
    
    // === Low End Mode ===
    public boolean lowEndMode = false;
    public boolean reduceParticles = false;
    public boolean optimizeClouds = false;
    public boolean optimizeWeather = false;
    public boolean optimizeTransparency = false;
    public boolean optimizeEntityRendering = false;
    public boolean optimizeChunkUpdates = false;
    public boolean disableExpensiveEffects = false;
    
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = new File("config/midpoint-optimizer.json").toPath();
    
    public static ModConfig load() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                return GSON.fromJson(reader, ModConfig.class);
            } catch (IOException e) {
                MidpointOptimizer.LOGGER.error("Failed to load config", e);
            }
        }
        ModConfig config = new ModConfig();
        config.save();
        return config;
    }
    
    public void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            MidpointOptimizer.LOGGER.error("Failed to save config", e);
        }
    }
    
    public void enableLowEndMode() {
        lowEndMode = true;
        reduceParticles = true;
        optimizeClouds = true;
        optimizeWeather = true;
        optimizeTransparency = true;
        optimizeEntityRendering = true;
        optimizeChunkUpdates = true;
        disableExpensiveEffects = true;
        targetFps = 30;
        save();
    }
    
    public void disableLowEndMode() {
        lowEndMode = false;
        reduceParticles = false;
        optimizeClouds = false;
        optimizeWeather = false;
        optimizeTransparency = false;
        optimizeEntityRendering = false;
        optimizeChunkUpdates = false;
        disableExpensiveEffects = false;
        targetFps = 60;
        save();
    }
  }
