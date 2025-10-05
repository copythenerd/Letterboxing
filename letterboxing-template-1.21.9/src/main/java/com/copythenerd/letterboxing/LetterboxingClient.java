package com.copythenerd.letterboxing;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import static com.copythenerd.letterboxing.Letterboxing.MOD_ID;
import static com.copythenerd.letterboxing.config.LetterboxingConfig.getInstance;

public class LetterboxingClient implements ClientModInitializer {

    public void onInitializeClient() {

        if (getInstance().getCarvedPumpkinVisibility())
        {
            Boolean registered = ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of("pumpkinbluroverwrite"),
                    FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(), ResourcePackActivationType.ALWAYS_ENABLED);
            Letterboxing.LOGGER.info("Registered Resource Pack: " + String.valueOf(registered));

        }
    }
}

//