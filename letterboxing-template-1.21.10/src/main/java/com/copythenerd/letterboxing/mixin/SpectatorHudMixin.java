package com.copythenerd.letterboxing.mixin;

import com.copythenerd.letterboxing.Letterboxing;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.SpectatorHud;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static com.copythenerd.letterboxing.config.LetterboxingConfig.getInstance;

@Mixin(SpectatorHud.class)
public abstract class SpectatorHudMixin {
    @Unique
    MinecraftClient client = MinecraftClient.getInstance();

    @Dynamic
    @ModifyVariable(at = @At("HEAD"), method = "renderSpectatorMenu(Lnet/minecraft/client/gui/DrawContext;FIILnet/minecraft/client/gui/hud/spectator/SpectatorMenuState;)V",
            ordinal = 1, argsOnly = true)
    private int injected(int y) {
        if (getInstance().getAdjustPlayerHud())
        {
            return y - Letterboxing.horizontalBarHeight;
        } else
        {
            return y;
        }
    }

    // Variable l
    @Dynamic
    @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowHeight()I"))
    private int modifySpectatorText(int value) {
        if (getInstance().getAdjustPlayerHud())
        {
            return client.getWindow().getScaledHeight() - Letterboxing.horizontalBarHeight;
        } else
        {
            return value;
        }
    }
}