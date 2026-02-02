package com.example.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.server.world.ThreadedAnvilChunkStorage")
public class ChunkStreamerMixin {

    @Inject(method = "getChebyshevDistance", at = @At("RETURN"), cancellable = true)
    private static void prioritizeForwardChunks(ChunkPos pos, ServerPlayerEntity player, boolean useCameraPosition, CallbackInfoReturnable<Integer> cir) {
        int originalDistance = cir.getReturnValue();
        Vec3d velocity = player.getVelocity();
        double speedSqr = velocity.lengthSquared();


        if (speedSqr > 0.002) {
            double speed = Math.sqrt(speedSqr);


            double dx = pos.getCenterX() - player.getX();
            double dz = pos.getCenterZ() - player.getZ();
            double distanceToChunk = Math.sqrt(dx * dx + dz * dz);

            // Player direction
            float yaw = player.getYaw() * 0.017453292F;
            double dirX = -MathHelper.sin(yaw);
            double dirZ = MathHelper.cos(yaw);

            // Dot product to find if the chunk is in front (1.0) or behind (-1.0)
            double dot = (dx * dirX + dz * dirZ) / distanceToChunk;

            // DYNAMIC SCALING MATH
            // Boost: Up to 6 chunks extra distance at high speed (Elytra)
            // Penalty: Chunks behind you are treated as 4 chunks further away
            if (dot > 0.4) {

                double boost = Math.min(6.0, speed * 10.0) * dot;
                cir.setReturnValue(Math.max(0, (int)(originalDistance - boost)));
            } else if (dot < -0.5) {

                int penalty = 4;
                cir.setReturnValue(originalDistance + penalty);
            }
        }
    }
}