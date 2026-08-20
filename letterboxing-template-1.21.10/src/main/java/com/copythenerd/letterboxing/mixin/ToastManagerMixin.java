package com.copythenerd.letterboxing.mixin;

import com.copythenerd.letterboxing.Letterboxing;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.ToastManager;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import static com.copythenerd.letterboxing.config.LetterboxingConfig.getInstance;

@Mixin(ToastManager.class)
public abstract class ToastManagerMixin {
    @Unique
    MinecraftClient client = MinecraftClient.getInstance();

    // Variable l
    @Dynamic
    @ModifyExpressionValue(method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowWidth()I"))
    private int modifyDraw(int value) {
        if (getInstance().getAdjustToasts())
        {
            // This now subtracts from the getScaledWindowHeight()-call
            return client.getWindow().getScaledWidth() - Letterboxing.verticalBarWidth;
        } else
        {
            return value;
        }
    }


}