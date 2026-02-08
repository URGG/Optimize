package com.example;

import com.example.client.StreamerHudRenderer; // <--- MUST be this package
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class ExampleModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This registers the code that draws the text
        HudRenderCallback.EVENT.register(new StreamerHudRenderer());
    }
}