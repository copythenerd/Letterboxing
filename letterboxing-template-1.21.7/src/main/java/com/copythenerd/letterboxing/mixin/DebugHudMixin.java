package com.copythenerd.letterboxing.mixin;

import com.copythenerd.letterboxing.Letterboxing;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static com.copythenerd.letterboxing.config.LetterboxingConfig.getInstance;

@Mixin(DebugHud.class)
public abstract class DebugHudMixin {
    @Unique
    MinecraftClient client = MinecraftClient.getInstance();

    // Modifies both window widths inside render()
    @Dynamic
    @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowWidth()I"))
    private int modifyWindowWidthRender(int value) {
        if (getInstance().getAdjustDebug())
        {
            return client.getWindow().getScaledWidth() - Letterboxing.verticalBarWidth;
        } else
        {
            return value;
        }
    }

    // Modifies both window widths in drawText()
    @Dynamic
    @ModifyExpressionValue(method = "drawText", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowWidth()I"))
    private int modifyWindowWidthDrawText(int value) {
        if (getInstance().getAdjustDebug())
        {
            return client.getWindow().getScaledWidth() - Letterboxing.verticalBarWidth;
        } else
        {
            return value;
        }
    }

    // Modifies the value 2 inside the first ternary expression
    @Dynamic
    @ModifyConstant(method = "drawText", constant = @Constant(intValue = 2, ordinal = 0))
    private int modifyWindowWidthDrawText2(int value) {
        if (getInstance().getAdjustDebug())
        {
            return 2 + Letterboxing.verticalBarWidth;
        } else
        {
            return value;
        }
    }

    // Modifies the value 2 inside the second ternary expression
    @Dynamic
    @ModifyConstant(method = "drawText", constant = @Constant(intValue = 2, ordinal = 3))
    private int modifyWindowWidthDrawText3(int value) {
        if (getInstance().getAdjustDebug())
        {
            return 2 + Letterboxing.verticalBarWidth;
        } else
        {
            return value;
        }
    }

    // Modifies the value 2 in the assignment of m in the first for-loop
    @Dynamic
    @ModifyConstant(method = "drawText", constant = @Constant(intValue = 2, ordinal = 2))
    private int modifyM(int value) {
        if (getInstance().getAdjustDebug())
        {
            return 2 + Letterboxing.horizontalBarHeight;
        } else
        {
            return value;
        }
    }

    // Modifies the value 2 in the assignment of m in the first for-loop
    @Dynamic
    @ModifyConstant(method = "drawText", constant = @Constant(intValue = 2, ordinal = 5))
    private int modifyM2(int value) {
        if (getInstance().getAdjustDebug())
        {
            return 2 + Letterboxing.horizontalBarHeight;
        } else
        {
            return value;
        }
    }
}