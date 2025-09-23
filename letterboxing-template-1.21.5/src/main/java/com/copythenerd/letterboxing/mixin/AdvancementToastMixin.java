package com.copythenerd.letterboxing.mixin;

import com.copythenerd.letterboxing.Letterboxing;
import net.minecraft.client.toast.AdvancementToast;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static com.copythenerd.letterboxing.config.LetterboxingConfig.getInstance;

@Mixin(AdvancementToast.class)
public abstract class AdvancementToastMixin {
    // y above if-statement
    @Dynamic
    @ModifyArg(method = "draw",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Ljava/util/function/Function;Lnet/minecraft/util/Identifier;IIII)V",
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

    // First y inside if-statement
    @Dynamic
    @ModifyArg(method = "draw",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIIZ)I",
                    ordinal = 0), index = 3)
    private int modifySecondY(int y) {
        if (getInstance().getAdjustToasts())
        {
            return y + Letterboxing.horizontalBarHeight;
        } else
        {
            return y;
        }
    }

    // Second y inside if-statement
    @Dynamic
    @ModifyArg(method = "draw",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;IIIZ)I",
                    ordinal = 0), index = 3)
    private int modifyThirdY(int y) {
        if (getInstance().getAdjustToasts())
        {
            return y + Letterboxing.horizontalBarHeight;
        } else
        {
            return y;
        }
    }

    // First y below if-statement
    @Dynamic
    @ModifyArg(method = "draw",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawItemWithoutEntity(Lnet/minecraft/item/ItemStack;II)V",
                    ordinal = 0), index = 2)
    private int modifyFourthY(int y) {
        if (getInstance().getAdjustToasts())
        {
            return y + Letterboxing.horizontalBarHeight;
        } else
        {
            return y;
        }
    }


}