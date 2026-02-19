package com.example.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerChunkLoadingManager;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerChunkLoadingManager.class)
public class ChunkStreamerMixin {

    @Inject(method = "getSquaredDistanceToPoint", at = @At("RETURN"), cancellable = true)
    private static void optimizeChunkPriority(ChunkPos chunkPos, ServerPlayerEntity player, CallbackInfoReturnable<Double> cir) {
        double originalDistance = cir.getReturnValue();

        // 1. Get player's looking direction (rotation)
        Vec3d lookVec = player.getRotationVec(1.0F);

        // 2. Calculate vector from player to the chunk
        double dx = (chunkPos.getCenterX()) - player.getX();
        double dz = (chunkPos.getCenterZ()) - player.getZ();
        Vec3d chunkVec = new Vec3d(dx, 0, dz).normalize();

        // 3. Dot Product: 1.0 means chunk is directly in front, -1.0 means behind
        double dot = lookVec.x * chunkVec.x + lookVec.z * chunkVec.z;

        // 4. Apply Multiplier: If chunk is in front (dot > 0.5), make it "seem" closer
        // This tricks Minecraft into loading it sooner than chunks at the same distance behind you.
        if (dot > 0.4) {
            cir.setReturnValue(originalDistance * 0.5); // 50% "closer" priority
        } else if (dot < -0.2) {
            cir.setReturnValue(originalDistance * 1.5); // 50% "farther" priority
        }
    }
}