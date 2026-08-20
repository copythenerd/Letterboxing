package com.copythenerd.letterboxing.mixin;

import com.copythenerd.letterboxing.Letterboxing;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.SubtitlesHud;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static com.copythenerd.letterboxing.config.LetterboxingConfig.getInstance;


@Mixin(SubtitlesHud.class)
public abstract class SubtitlesHudMixin {
    MinecraftClient client = MinecraftClient.getInstance();

    // Variable n for hotbar attack indicator
    @Dynamic
    @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowHeight()I"))
    private int modifyWindowHeight(int value) {
        if (getInstance().getAdjustSubtitles())
        {
            // This now subtracts from the getScaledWindowHeight()-call
            return client.getWindow().getScaledHeight() - Letterboxing.horizontalBarHeight;
        } else
        {
            return value;
        }
    }

    @Dynamic
    @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowWidth()I"))
    private int modifyWindowWidth(int value) {
        if (getInstance().getAdjustSubtitles())
        {
            // This now subtracts from the getScaledWindowHeight()-call
            return client.getWindow().getScaledWidth() - Letterboxing.verticalBarWidth;
        } else
        {
            return value;
        }
    }

}