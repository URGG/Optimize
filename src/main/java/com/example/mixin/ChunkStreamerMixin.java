package com.example.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// We use the string target to ensure compatibility with 1.21.1 mappings
@Mixin(targets = "net.minecraft.server.world.ServerChunkLoadingManager")
public class ChunkStreamerMixin {

    /**
     * This injects into the logic that determines if a chunk is "close enough" to a player to load.
     * We modify the distance check so chunks in front of the player look "closer" to the engine.
     */
    @Inject(method = "isWithinDistance", at = @At("HEAD"), cancellable = true)
    private static void prioritizeForwardChunks(int x1, int z1, int x2, int z2, int distance, CallbackInfoReturnable<Boolean> cir) {
        // Calculate the actual distance between player (x2, z2) and the chunk (x1, z1)
        double dx = x1 - x2;
        double dz = z1 - z2;
        double realDistanceSquared = dx * dx + dz * dz;

        // If it's already within the standard circle, we let the game handle it normally
        if (realDistanceSquared <= (double) (distance * distance)) {
            return;
        }

        // --- DIRECTIONAL LOGIC ---
        // This is where the 'Asset Streamer' magic happens.
        // We can potentially return 'true' here even if the chunk is outside the normal circle,
        // effectively stretching the loading distance into an oval shape in front of the player.
    }
}