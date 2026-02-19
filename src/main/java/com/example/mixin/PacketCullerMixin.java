package com.example.mixin;

import net.minecraft.server.world.ServerChunkLoadingManager;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerChunkLoadingManager.class) // Simplified header
public class PacketCullerMixin {
    // Keeping this empty for now so you can actually enter the world
}