package com.example.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void renderStreamerStatus(DrawContext context, float tickDelta, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && !client.options.hudHidden) {
            double speed = Math.round(client.player.getVelocity().length() * 20 * 10.0) / 10.0;

            String status = "§b[Asset Streamer] §fActive";
            String speedText = "§7Speed: §e" + speed + " b/s";

            context.drawText(client.textRenderer, status, 10, 10, 0xFFFFFF, true);
            context.drawText(client.textRenderer, speedText, 10, 20, 0xFFFFFF, true);

            if (speed > 15) {
                context.drawText(client.textRenderer, "§6>> BOOSTING HORIZON <<", 10, 30, 0xFFFFFF, true);
            }
        }
    }
}