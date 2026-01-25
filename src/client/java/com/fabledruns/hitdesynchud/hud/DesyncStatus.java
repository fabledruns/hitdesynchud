package com.fabledruns.hitdesynchud.hud;

public enum DesyncStatus {
    SYNCED("✓ Synced", 0xFF55FF55),
    MINOR("⚠ Minor Desync", 0xFFFFFF55),
    SEVERE("⚠ Insane Lag", 0xFFFF5555);

    private final String message;
    private final int color;

    DesyncStatus(String message, int color) {
        this.message = message;
        this.color = color;
    }

    public String getMessage() {
        return message;
    }

    public int getColor() {
        return color;
    }
}
