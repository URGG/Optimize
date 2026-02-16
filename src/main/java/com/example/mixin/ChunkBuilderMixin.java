package com.example.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkBuilder.BuiltChunk.class)
public class ChunkBuilderMixin {

    @Inject(method = "getSquaredDistanceToCamera", at = @At("RETURN"), cancellable = true)
    private void boostFrontalRenderPriority(CallbackInfoReturnable<Double> cir) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        double dist = cir.getReturnValue();
        Vec3d lookDir = client.player.getRotationVec(1.0F);

        // Accessing the chunk origin via a cast
        BlockPos chunkPos = ((ChunkBuilder.BuiltChunk)(Object)this).getOrigin();
        Vec3d toChunk = new Vec3d(
                chunkPos.getX() - client.player.getX(),
                0,
                chunkPos.getZ() - client.player.getZ()
        ).normalize();

        double dot = lookDir.dotProduct(toChunk);

        // If chunk is in front (dot > 0.3), fake a closer distance
        // This moves it to the top of the 'To-Build' queue
        if (dot > 0.3) {
            cir.setReturnValue(dist * 0.1); // 90% priority boost
        }
    }
}