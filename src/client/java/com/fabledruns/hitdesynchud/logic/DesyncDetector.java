package com.fabledruns.hitdesynchud.logic;

import com.fabledruns.hitdesynchud.hud.DesyncStatus;
import com.fabledruns.hitdesynchud.hud.HudRenderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

@Environment(EnvType.CLIENT)
public class DesyncDetector {

    private static final Map<LivingEntity, Integer> lastHurtTime = new HashMap<>();
    private static final Map<LivingEntity, Long> swingTimestamps = new HashMap<>();

    public static void init() {}

    public static void registerSwing(LivingEntity entity) {
        lastHurtTime.put(entity, entity.hurtTime);
        swingTimestamps.put(entity, System.currentTimeMillis());
    }

    public static void tick() {
        long now = System.currentTimeMillis();
        Iterator<Map.Entry<LivingEntity, Long>> iterator = swingTimestamps.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<LivingEntity, Long> entry = iterator.next();
            LivingEntity entity = entry.getKey();
            long swingTime = entry.getValue();
            long elapsed = now - swingTime;

            int oldHurt = lastHurtTime.getOrDefault(entity, -1);

            if (entity.hurtTime != oldHurt) {
                // Hit registered!
                if (elapsed > 200) {
                    HudRenderer.updateStatus(DesyncStatus.MINOR);
                } else {
                    HudRenderer.updateStatus(DesyncStatus.SYNCED);
                }
                iterator.remove();
                lastHurtTime.remove(entity);
            } else if (elapsed > 300) {
                // Not registered after 300ms -> Severe Desync
                HudRenderer.updateStatus(DesyncStatus.SEVERE);
                iterator.remove();
                lastHurtTime.remove(entity);
            }
        }
    }
}
