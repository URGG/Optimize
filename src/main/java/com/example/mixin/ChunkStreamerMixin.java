package com.example.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.server.world.ServerChunkLoadingManager")
public class ChunkStreamerMixin {

    @Inject(method = "method_18727", at = @At("RETURN"), cancellable = true, remap = false)
    private static void optimizeChunkPriority(ChunkPos chunkPos, Entity entity, CallbackInfoReturnable<Double> cir) {
        if (!(entity instanceof ServerPlayerEntity player)) return;

        double originalDistance = cir.getReturnValue();
        Vec3d lookVec = player.getRotationVec(1.0F);

        double dx = chunkPos.getCenterX() - player.getX();
        double dz = chunkPos.getCenterZ() - player.getZ();
        Vec3d chunkVec = new Vec3d(dx, 0, dz).normalize();

        double dot = lookVec.x * chunkVec.x + lookVec.z * chunkVec.z;

        if (dot > 0.4) {
            cir.setReturnValue(originalDistance * 0.5);
        }
    }
}