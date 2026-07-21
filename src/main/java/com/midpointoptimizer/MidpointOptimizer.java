package com.midpointoptimizer;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MidpointOptimizer implements ClientModInitializer {
    public static final String MOD_ID = "midpoint-optimizer";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    @Override
    public void onInitializeClient() {
        LOGGER.info("Midpoint Optimizer v1.0.0 loaded successfully!");
    }
}
