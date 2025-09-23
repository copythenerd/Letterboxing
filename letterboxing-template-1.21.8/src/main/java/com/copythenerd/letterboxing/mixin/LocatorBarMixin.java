package com.copythenerd.letterboxing.mixin;

import net.minecraft.client.gui.hud.bar.LocatorBar;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static com.copythenerd.letterboxing.Letterboxing.horizontalBarHeight;
import static com.copythenerd.letterboxing.config.LetterboxingConfig.getInstance;

@Mixin(LocatorBar.class)
public class LocatorBarMixin {

    //Y-Position of Locator Bar
    @Dynamic
    @ModifyArg(method = "renderBar",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIII)V", ordinal = 3))
    private int modifyY(int value) {
        if (getInstance().getAdjustPlayerHud())
        {
            return value - horizontalBarHeight;
        } else
        {
            return value;
        }
    }
}
