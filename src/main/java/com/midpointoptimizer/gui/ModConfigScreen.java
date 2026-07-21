package com.midpointoptimizer.gui;

import com.midpointoptimizer.MidpointOptimizer;
import com.midpointoptimizer.config.ModConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ModConfigScreen extends Screen {
    private final Screen parent;
    private final ModConfig config;

    public ModConfigScreen(Screen parent) {
        super(Text.literal("MidpointOptimizer Config"));
        this.parent = parent;
        this.config = MidpointOptimizer.getInstance().getConfig();
    }

    @Override
    protected void init() {
        super.init();

        // === FPS Indicator Toggle ===
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("FPS Indicator: " + (config.fpsEnabled ? "ON" : "OFF")),
                button -> {
                    config.fpsEnabled = !config.fpsEnabled;
                    config.save();
                    button.setMessage(Text.literal("FPS Indicator: " + (config.fpsEnabled ? "ON" : "OFF")));
                })
                .dimensions(this.width / 2 - 100, 50, 200, 20)
                .build());

        // === Smart Optimizer Toggle ===
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Smart Optimizer: " + (config.smartOptimizerEnabled ? "ON" : "OFF")),
                button -> {
                    config.smartOptimizerEnabled = !config.smartOptimizerEnabled;
                    config.save();
                    button.setMessage(Text.literal("Smart Optimizer: " + (config.smartOptimizerEnabled ? "ON" : "OFF")));
                })
                .dimensions(this.width / 2 - 100, 80, 200, 20)
                .build());

        // === Target FPS ===
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Target FPS: " + config.targetFps),
                button -> {
                    if (config.targetFps >= 120) {
                        config.targetFps = 30;
                    } else {
                        config.targetFps += 15;
                    }
                    config.save();
                    button.setMessage(Text.literal("Target FPS: " + config.targetFps));
                })
                .dimensions(this.width / 2 - 100, 110, 200, 20)
                .build());

        // === Low End Mode Toggle ===
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Low End Mode: " + (config.lowEndMode ? "ON" : "OFF")),
                button -> {
                    if (config.lowEndMode) {
                        config.disableLowEndMode();
                    } else {
                        config.enableLowEndMode();
                    }
                    button.setMessage(Text.literal("Low End Mode: " + (config.lowEndMode ? "ON" : "OFF")));
                })
                .dimensions(this.width / 2 - 100, 150, 200, 20)
                .build());

        // === Back Button ===
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Back"),
                button -> this.client.setScreen(this.parent))
                .dimensions(this.width / 2 - 100, this.height - 30, 200, 20)
                .build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        
        // Info text
        context.drawCenteredTextWithShadow(this.textRenderer, 
            Text.literal("§7Click buttons to toggle settings"), 
            this.width / 2, 180, 0xAAAAAA);
        
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }
}
