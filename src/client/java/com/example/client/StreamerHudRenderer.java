package com.example.client;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;

public class StreamerHudRenderer implements HudRenderCallback {

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        // 1. GET ACTUAL GAME DATA
        // Calculate horizontal speed: Square root of (velocity X² + velocity Z²) * 20 ticks
        double xVel = client.player.getVelocity().x;
        double zVel = client.player.getVelocity().z;
        double speed = Math.sqrt(xVel * xVel + zVel * zVel) * 20;

        // Get the player's current Chunk coordinates
        int chunkX = client.player.getChunkPos().x;
        int chunkZ = client.player.getChunkPos().z;

        // 2. FORMAT THE DATA INTO STRINGS
        String speedDisplay = String.format("Speed: %.2f bps", speed);
        String chunkDisplay = "Chunk: " + chunkX + ", " + chunkZ;

        // 3. RENDER THE BOX
        int x = 10;
        int y = 10;
        drawContext.fill(x - 2, y - 2, x + 120, y + 38, 0x80000000); // Background

        // 4. RENDER THE ACTUAL DATA
        // We MUST use Text.literal() or the game will show nothing!
        drawContext.drawText(client.textRenderer, Text.literal("§bASSET STREAMER"), x, y, 0xFFFFFF, true);
        drawContext.drawText(client.textRenderer, Text.literal(speedDisplay), x, y + 12, 0xFFFFFF, true);
        drawContext.drawText(client.textRenderer, Text.literal(chunkDisplay), x, y + 24, 0xFFFFFF, true);
    }
}