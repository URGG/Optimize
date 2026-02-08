package com.example.client;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

public class StreamerHudRenderer implements HudRenderCallback {

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.options.hudHidden) return;

        // Position
        int x = 20;
        int y = 20;


        drawContext.fill(x - 5, y - 5, x + 100, y + 30, 0xAA000000); // Semi-transparent black
        drawContext.fill(x - 5, y - 5, x - 3, y + 30, 0xFF55FF55);   // Green bar

        // 2. FORCE TEXT TO THE FRONT
        // We use drawTextWithShadow because it bypasses some transparency bugs
        // Color 0xFFFFFF is pure white.
        drawContext.drawTextWithShadow(client.textRenderer, "DEBUG: MOD ACTIVE", x, y, 0xFFFFFF);
        drawContext.drawTextWithShadow(client.textRenderer, "COORDS: " + client.player.getBlockPos().toShortString(), x, y + 12, 0x55FF55);
    }
}