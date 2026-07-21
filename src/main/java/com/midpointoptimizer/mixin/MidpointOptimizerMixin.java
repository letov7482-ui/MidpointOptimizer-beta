package com.midpointoptimizer.mixin;

import net.minecraft.class_763;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_763.class)
public class MidpointOptimizerMixin {
    @Inject(method = "method_3410", at = @At("HEAD"))
    private void onRender(CallbackInfo ci) {
        // Optimization hooks
    }
}
