package com.example.mixin;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(targets = "net.minecraft.server.world.ThreadedAnvilChunkStorage")
public class PacketCullerMixin {

    @Inject(method = "canSkipChunk", at = @At("head"),cancellable = true)
    private void discardBackwardsPackets(ServerPlayerEntity player, ChunkPos pos, CallbackInfoReturnable<Boolean> cir){
        if(player.getVelocity().lengthSquared() > 0.05){
            double dx = pos.getCenterX() - player.getX();
            double dz = pos.getCenterZ() - player.getZ();

            float yaw = player.getYaw() * 0.017543292F;
            double dirX = -MathHelper.sin(yaw) ;
            double dirZ = MathHelper.cos(yaw) ;

            double dot = (dx *dirX + dz * dirZ) / Math.sqrt(dx * dx + dz * dz);

            if(dot < -0.2){
                cir.setReturnValue(true);
            }
        }
    }
}
