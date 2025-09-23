package com.copythenerd.letterboxing.mixin;

import com.copythenerd.letterboxing.Letterboxing;
import net.minecraft.client.toast.TutorialToast;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import static com.copythenerd.letterboxing.Letterboxing.horizontalBarHeight;
import static com.copythenerd.letterboxing.config.LetterboxingConfig.getInstance;

@Mixin(TutorialToast.class)
public abstract class TutorialToastMixin {

    @Dynamic
    @ModifyArg(method = "draw",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIII)V"),
            index = 3)
    private int modifyDrawGuiTexture(int y) {
        if (getInstance().getAdjustToasts())
        {
            return Letterboxing.horizontalBarHeight;
        } else
        {
            return y;
        }
    }

    @Dynamic
    @ModifyArg(method = "draw",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/toast/TutorialToast$Type;drawIcon(Lnet/minecraft/client/gui/DrawContext;II)V"), index = 2)
    private int modifyDrawIcon(int y) {
        if (getInstance().getAdjustToasts())
        {
            return y + Letterboxing.horizontalBarHeight;
        } else
        {
            return y;
        }
    }

    // Variable k
    @Dynamic
    @ModifyArg(method = "draw",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;IIIZ)V"),
            index = 3)
    private int modifyK(int k) {
        if (getInstance().getAdjustToasts())
        {
            return k + Letterboxing.horizontalBarHeight;
        } else
        {
            return k;
        }
    }

    // Variable l
    @Dynamic
    @ModifyArgs(method = "draw",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V"))
    private void modifySleepOverlay(Args args) {
        if (getInstance().getAdjustPlayerHud())
        {
            args.set(1, (int) args.get(1) + horizontalBarHeight);
            args.set(3, (int) args.get(3) + horizontalBarHeight);
        }
    }
}