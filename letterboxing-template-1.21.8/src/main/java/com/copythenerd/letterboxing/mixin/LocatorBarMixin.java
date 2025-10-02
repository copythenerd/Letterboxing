package com.copythenerd.letterboxing.mixin;

import com.copythenerd.letterboxing.Letterboxing;
import net.minecraft.client.gui.hud.bar.LocatorBar;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static com.copythenerd.letterboxing.Letterboxing.horizontalBarHeight;
import static com.copythenerd.letterboxing.config.LetterboxingConfig.getInstance;

@Mixin(LocatorBar.class)
public class LocatorBarMixin {

    //i
    @Dynamic
    @ModifyVariable(at = @At("STORE"), method = "renderAddons", ordinal = 0)
    private int modifyI(int i) {
        if (getInstance().getAdjustPlayerHud())
        {
            return i - Letterboxing.horizontalBarHeight;
        } else
        {
            return i;
        }
    }

    //Y-Position of Locator Bar
    @Dynamic
    @ModifyArg(method = "renderBar",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIII)V"), index = 3)
    private int modifyYYY(int value) {
        if (getInstance().getAdjustPlayerHud())
        {
            return value - horizontalBarHeight;
        } else
        {
            return value;
        }
    }
}
