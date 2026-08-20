package com.copythenerd.letterboxing.mixin;

import com.copythenerd.letterboxing.Letterboxing;
import net.minecraft.client.toast.RecipeToast;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static com.copythenerd.letterboxing.config.LetterboxingConfig.getInstance;


@Mixin(RecipeToast.class)
public abstract class RecipeToastMixin {

    // First y
    @Dynamic
    @ModifyArg(method = "draw",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIII)V",
                    ordinal = 0), index = 3)
    private int modifyFirstY(int value) {
        if (getInstance().getAdjustToasts())
        {
            return Letterboxing.horizontalBarHeight;
        } else
        {
            return value;
        }
    }

    // Second y
    @Dynamic
    @ModifyArg(method = "draw",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIIZ)V",
                    ordinal = 0), index = 3)
    private int modifyTitle(int y) {
        if (getInstance().getAdjustToasts())
        {
            return y + Letterboxing.horizontalBarHeight;
        } else
        {
            return y;
        }
    }

    // Third y
    @Dynamic
    @ModifyArg(method = "draw",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIIZ)V",
                    ordinal = 1), index = 3)
    private int modifyDescription(int y) {
        if (getInstance().getAdjustToasts())
        {
            return y + Letterboxing.horizontalBarHeight;
        } else
        {
            return y;
        }
    }

    // Fourth y
    @Dynamic
    @ModifyArg(method = "draw",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawItemWithoutEntity(Lnet/minecraft/item/ItemStack;II)V",
                    ordinal = 0), index = 2)
    private int modifyCategoryItem(int y) {
        // Scaling with float considers the scaling done by getMatrices().scale()
        if (getInstance().getAdjustToasts())
        {
            return y + (int) (5.0F / 3.0F * Letterboxing.horizontalBarHeight);
        } else
        {
            return y;
        }
    }

    // Fifth y
    @Dynamic
    @ModifyArg(method = "draw",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawItemWithoutEntity(Lnet/minecraft/item/ItemStack;II)V",
                    ordinal = 1), index = 2)
    private int modifyUnlockedItem(int y) {
        if (getInstance().getAdjustToasts())
        {
            return y + Letterboxing.horizontalBarHeight;
        } else
        {
            return y;
        }
    }

}