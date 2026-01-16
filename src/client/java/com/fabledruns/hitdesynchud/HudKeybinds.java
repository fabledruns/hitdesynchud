package com.fabledruns.hitdesynchud;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class HudKeybinds {
    public static KeyBinding toggleEditor;
    public static KeyBinding moveLeft, moveRight, moveUp, moveDown;

    public static void init() {
        // Register keybind for toggle editor
        toggleEditor = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Toggle Editor",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                "Hit Desync HUD"
        ));
        // Register keybinds for moving the HUD
        moveLeft  = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Move HUD Left",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT,
                "Hit Desync HUD"
        ));

        moveRight = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Move HUD Right",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT,
                "Hit Desync HUD"
        ));

        moveUp    = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Move HUD Up",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UP,
                "Hit Desync HUD"
        ));

        moveDown  = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Move HUD Down",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_DOWN,
                "Hit Desync HUD"
        ));
    }

    public static void tick() {
        while (toggleEditor.wasPressed()) HudConfig.editMode = !HudConfig.editMode;

        if (!HudConfig.editMode) return;
        // Shows the HUD when in edit mode
        HudRenderer.showDesyncIcon(2000);

        if (moveLeft.isPressed())  HudConfig.x -= 1;
        if (moveRight.isPressed()) HudConfig.x += 1;
        if (moveUp.isPressed())    HudConfig.y -= 1;
        if (moveDown.isPressed())  HudConfig.y += 1;
    }
}
