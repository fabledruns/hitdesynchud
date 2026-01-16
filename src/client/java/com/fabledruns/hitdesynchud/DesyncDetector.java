package com.fabledruns.hitdesynchud;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class DesyncDetector {

    private static final Map<LivingEntity, Integer> lastHurtTime = new HashMap<>();
    private static final Map<LivingEntity, Long> swingTimestamps = new HashMap<>();

    public static void init() {} // could add tick cleanup if needed

    public static void registerSwing(LivingEntity entity) {
        lastHurtTime.put(entity, entity.hurtTime);
        swingTimestamps.put(entity, System.currentTimeMillis());
    }

    public static void tick() {
        long now = System.currentTimeMillis();

        for (Map.Entry<LivingEntity, Long> entry : swingTimestamps.entrySet()) {
            LivingEntity entity = entry.getKey();
            long swingTime      = entry.getValue();

            if (now - swingTime > 300) {
                int oldHurt = lastHurtTime.getOrDefault(entity, -1);
                if (entity.hurtTime == oldHurt) {
                    // The server did not register the hit
                    HudRenderer.showDesyncIcon(3000);
                }
                swingTimestamps.remove(entity);
                lastHurtTime.remove(entity);
                break;
            }
        }
    }
}
