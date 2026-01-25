package com.fabledruns.hitdesynchud;

import com.fabledruns.hitdesynchud.config.HudConfig;
import com.fabledruns.hitdesynchud.hud.HudRenderer;
import com.fabledruns.hitdesynchud.input.HudKeybinds;
import com.fabledruns.hitdesynchud.logic.AttackTracker;
import com.fabledruns.hitdesynchud.logic.DesyncDetector;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class HitDesyncHudClient implements ClientModInitializer {
    public static final String MOD_ID = "hitdesynchud";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        // load config
        LOGGER.info("Loading Hud Config");
        HudConfig.load();
        // init keybinds
        LOGGER.info("Initializing Hud Keybinds");
        HudKeybinds.init();
        // init renderer
        LOGGER.info("Initializing Hud Renderer for Drawing HUD");
        HudRenderer.init();
        // init attack tracker
        LOGGER.info("Initializing Attack Tracker");
        AttackTracker.init();
        // init detector
        LOGGER.info("Initializing Desync Detector");
        DesyncDetector.init();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            HudKeybinds.tick();
            DesyncDetector.tick();
        });
    }
}
