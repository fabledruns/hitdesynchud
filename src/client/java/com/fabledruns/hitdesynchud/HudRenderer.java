package com.fabledruns.hitdesynchud;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class HudRenderer {

    private static int timer = 0;

    public static void init() {
        HudRenderCallback.EVENT.register(HudRenderer::renderHud);
    }

    // Method to show the desync message
    public static void showDesyncIcon(int durationMs) { timer = durationMs; }

    private static void renderHud(DrawContext context, RenderTickCounter tickDelta) {
        if (timer > 0) {
            String message = "⚠ Hit Desync Detected"; // Warning Message
            MinecraftClient client = MinecraftClient.getInstance();
            TextRenderer font = client.textRenderer;

            // Get width and height of the minecraft client
            int width  = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();

            if (HudConfig.editMode) {
                // Movement of the hud while in editor mode
                if (HudKeybinds.moveLeft.isPressed())  HudConfig.x -= 1;
                if (HudKeybinds.moveRight.isPressed()) HudConfig.x += 1;
                if (HudKeybinds.moveUp.isPressed())    HudConfig.y -= 1;
                if (HudKeybinds.moveDown.isPressed())  HudConfig.y += 1;

                context.fill(
                        width / 2 + HudConfig.x - 64,
                        height / 2 + HudConfig.y - 5,
                        width / 2 + HudConfig.x + 64,
                        height / 2 + HudConfig.y + 15,
                        0x80FFFFFF
                );
            }

            // Draw warning message
            context.drawText(
                    font,
                    Text.literal(message),
                    width / 2 + HudConfig.x - font.getWidth(message) / 2,
                    height / 2 + HudConfig.y,
                    0xFFFF5555,
                    false
            );

            timer -= 16;
        }
    }
}
