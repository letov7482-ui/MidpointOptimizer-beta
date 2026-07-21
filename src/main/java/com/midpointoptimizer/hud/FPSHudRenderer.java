package com.midpointoptimizer.hud;

import com.midpointoptimizer.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.Queue;

public class FPSHudRenderer implements HudRenderCallback {
    private final ModConfig config;
    private final DecimalFormat df = new DecimalFormat("#.#");
    private final Queue<Integer> fpsHistory = new LinkedList<>();
    private int frameCounter = 0;
    private long lastSecond = System.currentTimeMillis();
    private int currentFps = 0;
    private float averageFps = 0;
    private long frameTime = 0;
    
    public FPSHudRenderer(ModConfig config) {
        this.config = config;
    }
    
    @Override
    public void onHudRender(DrawContext drawContext, net.minecraft.client.render.RenderTickCounter tickCounter) {
        if (!config.fpsEnabled) return;
        
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options == null) return;
        
        updateFps();
        
        MatrixStack matrices = drawContext.getMatrices();
        matrices.push();
        matrices.scale(config.hudScale, config.hudScale, 1.0f);
        
        int x = (int)(config.hudX / config.hudScale);
        int y = (int)(config.hudY / config.hudScale);
        int lineHeight = 12;
        int textColor = config.textColor;
        
        // Draw background
        if (config.showBackground) {
            int width = 0;
            int lines = 0;
            if (config.showCurrentFps) lines++;
            if (config.showAverageFps) lines++;
            if (config.showFrameTime) lines++;
            if (config.showMemoryUsage) lines++;
            if (config.showCpuUsage) lines++;
            
            if (lines > 0) {
                String[] texts = getTexts();
                for (String text : texts) {
                    width = Math.max(width, client.textRenderer.getWidth(text) + 20);
                }
                int height = lines * lineHeight + 10;
                
                drawContext.fill(
                    x - 5,
                    y - 5,
                    x + width + 5,
                    y + height + 5,
                    (config.backgroundAlpha << 24) | 0x000000
                );
            }
        }
        
        // Draw text
        int currentY = y;
        if (config.showCurrentFps) {
            drawText(drawContext, client, "FPS: " + currentFps, x, currentY, textColor);
            currentY += lineHeight;
        }
        if (config.showAverageFps) {
            drawText(drawContext, client, "Avg FPS: " + df.format(averageFps), x, currentY, textColor);
            currentY += lineHeight;
        }
        if (config.showFrameTime) {
            drawText(drawContext, client, "Frame: " + df.format(frameTime) + "ms", x, currentY, textColor);
            currentY += lineHeight;
        }
        if (config.showMemoryUsage) {
            Runtime runtime = Runtime.getRuntime();
            long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1048576;
            long maxMemory = runtime.maxMemory() / 1048576;
            drawText(drawContext, client, "Memory: " + usedMemory + "/" + maxMemory + "MB", x, currentY, textColor);
            currentY += lineHeight;
        }
        if (config.showCpuUsage) {
            drawText(drawContext, client, "CPU: " + getCpuUsage() + "%", x, currentY, textColor);
            currentY += lineHeight;
        }
        
        matrices.pop();
    }
    
    private void drawText(DrawContext drawContext, MinecraftClient client, String text, int x, int y, int color) {
        if (config.showShadow) {
            drawContext.drawText(client.textRenderer, text, x, y, color, true);
        } else {
            drawContext.drawText(client.textRenderer, text, x, y, color, false);
        }
    }
    
    private void updateFps() {
        long currentTime = System.currentTimeMillis();
        frameCounter++;
        
        if (currentTime - lastSecond >= 1000) {
            currentFps = frameCounter;
            frameCounter = 0;
            lastSecond = currentTime;
            
            fpsHistory.add(currentFps);
            if (fpsHistory.size() > 60) {
                fpsHistory.poll();
            }
            
            averageFps = (float) fpsHistory.stream().mapToInt(Integer::intValue).average().orElse(0);
            frameTime = fpsHistory.isEmpty() ? 0 : 1000 / fpsHistory.stream().mapToInt(Integer::intValue).average().orElse(0);
        }
    }
    
    private String[] getTexts() {
        int count = 0;
        if (config.showCurrentFps) count++;
        if (config.showAverageFps) count++;
        if (config.showFrameTime) count++;
        if (config.showMemoryUsage) count++;
        if (config.showCpuUsage) count++;
        
        String[] texts = new String[count];
        int i = 0;
        if (config.showCurrentFps) texts[i++] = "FPS: " + currentFps;
        if (config.showAverageFps) texts[i++] = "Avg FPS: " + df.format(averageFps);
        if (config.showFrameTime) texts[i++] = "Frame: " + df.format(frameTime) + "ms";
        if (config.showMemoryUsage) {
            Runtime runtime = Runtime.getRuntime();
            long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1048576;
            long maxMemory = runtime.maxMemory() / 1048576;
            texts[i++] = "Memory: " + usedMemory + "/" + maxMemory + "MB";
        }
        if (config.showCpuUsage) {
            texts[i++] = "CPU: " + getCpuUsage() + "%";
        }
        return texts;
    }
    
    private int getCpuUsage() {
        try {
            java.io.File file = new java.io.File("/proc/stat");
            if (!file.exists()) return 0;
            
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file));
            String line = reader.readLine();
            reader.close();
            
            if (line == null || !line.startsWith("cpu ")) return 0;
            
            String[] parts = line.split("\\s+");
            if (parts.length < 5) return 0;
            
            long user = Long.parseLong(parts[1]);
            long nice = Long.parseLong(parts[2]);
            long system = Long.parseLong(parts[3]);
            long idle = Long.parseLong(parts[4]);
            
            long total = user + nice + system + idle;
            long idleTotal = idle;
            
            if (total == 0) return 0;
            return (int)(100 - (idleTotal * 100.0 / total));
        } catch (Exception e) {
            return 0;
        }
    }
    }
