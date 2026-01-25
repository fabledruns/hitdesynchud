package com.fabledruns.hitdesynchud.config;

import com.fabledruns.hitdesynchud.hud.HudRenderer;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;

public class HudConfigScreen extends Screen {

    private final Screen parent;
    private boolean dragging = false;
    private int dragStartX, dragStartY;
    private int startX, startY;

    public HudConfigScreen(Screen parent) {
        super(Text.literal("Hit Desync HUD Config"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int w = this.width;
        int h = this.height;
        int yStart = h / 2 + 20;

            super.init();

            ScreenMouseEvents.afterMouseClick(this).register((screen, mouseX, mouseY, button) ->
                handleMouseClicked(mouseX, mouseY, button)
            );
            ScreenMouseEvents.afterMouseRelease(this).register((screen, mouseX, mouseY, button) ->
                handleMouseReleased(mouseX, mouseY, button)
            );
            ScreenMouseEvents.afterMouseDrag(this).register((screen, mouseX, mouseY, button, deltaX, deltaY) ->
                handleMouseDragged(mouseX, mouseY, button, deltaX, deltaY)
            );
        this.addDrawableChild(new FloatSliderWidget(w / 2 - 100, yStart, 200, 20, Text.literal("Scale: " + String.format("%.2fx", HudConfig.scale)), (HudConfig.scale - 0.5f) / 1.5f) {
            @Override
            protected void updateMessage() {
                this.setMessage(Text.literal("Scale: " + String.format("%.2fx", HudConfig.scale)));
            }

            @Override
            protected void applyValue() {
                HudConfig.scale = (float) (this.value * 1.5d + 0.5d);
            }
        });

        // Opacity Slider
        this.addDrawableChild(new FloatSliderWidget(w / 2 - 100, yStart + 24, 200, 20, Text.literal("Opacity: " + String.format("%.0f%%", HudConfig.opacity * 100)), HudConfig.opacity) {
            @Override
            protected void updateMessage() {
                this.setMessage(Text.literal("Opacity: " + String.format("%.0f%%", HudConfig.opacity * 100)));
            }

            @Override
            protected void applyValue() {
                HudConfig.opacity = (float) this.value;
            }
        });

        // Auto Hide Toggle
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(HudConfig.autoHide)
                .build(w / 2 - 100, yStart + 48, 200, 20, Text.literal("Auto-Hide"), (button, value) -> HudConfig.autoHide = value));

        // Background Toggle
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(HudConfig.showBackground)
                .build(w / 2 - 100, yStart + 72, 200, 20, Text.literal("Show Background"), (button, value) -> HudConfig.showBackground = value));

        // Done Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Done"), button -> {
            HudConfig.save();
            this.client.setScreen(this.parent);
        }).dimensions(w / 2 - 100, h - 30, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Draw background (manual fill to avoid blur crash)
        context.fill(0, 0, this.width, this.height, 0x80000000);
        
        // Draw the HUD preview
        // Pass a dummy value for status since we just want to see position/style
        // We force it to be visible by simulating a state
        HudRenderer.renderHudDemo(context);

        // Instructions
        context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("Click and drag the HUD to move"), this.width / 2, 20, 0xFFFFFF);
        
        super.render(context, mouseX, mouseY, delta);
    }

    private void handleMouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            if (HudRenderer.isMouseOverHud(mouseX, mouseY)) {
                dragging = true;
                dragStartX = (int) mouseX;
                dragStartY = (int) mouseY;
                startX = HudConfig.x;
                startY = HudConfig.y;
                return;
            }
        }
        return;
    }

    private void handleMouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (dragging) {
            int dx = (int) (mouseX - dragStartX);
            int dy = (int) (mouseY - dragStartY);
            
            HudConfig.x = startX + dx;
            HudConfig.y = startY + dy;
            
            // Snap to corners (threshold 20 pixels)
            snapToCorners();
            
            return;
        }
        return;
    }

    private void handleMouseReleased(double mouseX, double mouseY, int button) {
        dragging = false;
        return;
    }

    private void snapToCorners() {
        int threshold = 20;
        int screenWidth = this.client.getWindow().getScaledWidth();
        int screenHeight = this.client.getWindow().getScaledHeight();
        
        // Center of HUD approx (assuming small size or calculated in Renderer)
        // Renderer draws relative to CENTER of screen + x/y.
        // Screen Center
        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;

        int hudX = centerX + HudConfig.x;
        int hudY = centerY + HudConfig.y;
        
        // Snap logic: if hudX near 0 (left) or screenWidth (right)
        // But x is offset from center.
        // Left edge: hudX -> 0  => HudConfig.x -> -centerX
        // Right edge: hudX -> screenWidth => HudConfig.x -> screenWidth - centerX
        
        if (Math.abs(hudX - 20) < threshold) HudConfig.x = -centerX + 20; // Left (padded)
        if (Math.abs(hudX - (screenWidth - 20)) < threshold) HudConfig.x = centerX - 20; // Right

        if (Math.abs(hudY - 20) < threshold) HudConfig.y = -centerY + 20; // Top
        if (Math.abs(hudY - (screenHeight - 20)) < threshold) HudConfig.y = centerY - 20; // Bottom
    }

    private abstract static class FloatSliderWidget extends SliderWidget {
        public FloatSliderWidget(int x, int y, int width, int height, Text text, double value) {
            super(x, y, width, height, text, value);
        }
        
        @Override
        public void appendClickableNarrations(net.minecraft.client.gui.screen.narration.NarrationMessageBuilder builder) {
           super.appendClickableNarrations(builder);
        }
    }
}
