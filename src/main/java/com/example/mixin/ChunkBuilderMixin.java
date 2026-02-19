package com.example.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.client.render.chunk.ChunkBuilder$BuiltChunk")
public abstract class ChunkBuilderMixin {

    @Shadow public abstract BlockPos getOrigin();

    @Inject(method = "method_3439", at = @At("RETURN"), cancellable = true, remap = false)
    private void boostFrontalRenderPriority(CallbackInfoReturnable<Double> cir) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        double dist = cir.getReturnValue();
        Vec3d lookDir = client.player.getRotationVec(1.0F);
        BlockPos pos = this.getOrigin();

        Vec3d toChunk = new Vec3d(pos.getX() - client.player.getX(), 0, pos.getZ() - client.player.getZ()).normalize();
        double dot = lookDir.dotProduct(toChunk);

        if (dot > 0.3) {
            cir.setReturnValue(dist * 0.1);
        }
    }
}