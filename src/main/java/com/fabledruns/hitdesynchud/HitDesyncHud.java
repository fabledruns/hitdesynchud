package com.fabledruns.hitdesynchud;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HitDesyncHud implements ModInitializer {
	public static final String MOD_ID = "hitdesynchud";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        LOGGER.info("Initializing Hit Desync HUD");
        // Automatically initialized in the client side java file
    }
}