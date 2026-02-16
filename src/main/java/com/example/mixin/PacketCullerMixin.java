package com.example.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.server.world.ServerChunkLoadingManager")
public class PacketCullerMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTickEnd(CallbackInfo ci) {



    }
}