package com.copythenerd.letterboxing.mixin;

import com.copythenerd.letterboxing.Letterboxing;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import static com.copythenerd.letterboxing.config.LetterboxingConfig.getInstance;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin {
    @Unique
    MinecraftClient client = MinecraftClient.getInstance();

    // Value k
    @Dynamic
    @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;ceil(F)I"))
    private int modifyK(int value) {
        if (getInstance().getAdjustChat())
        {
            return value + Letterboxing.verticalBarWidth;
        } else
        {
            return value;
        }
    }

    // Modifies both window widths inside render()
    @Dynamic
    @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowHeight()I"))
    private int modifyWindowHeightRender(int value) {
        if (getInstance().getAdjustChat())
        {
            return client.getWindow().getScaledHeight() - Letterboxing.horizontalBarHeight;

        } else
        {
            return value;
        }
    }

    @Dynamic
    @ModifyArgs(method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;III)I"))
    private void modifyDrawTextWithShadowOrdered(Args args) {
        if (getInstance().getAdjustChat())
        {
            args.set(2, Letterboxing.verticalBarWidth);
        }
    }

    @Dynamic
    @ModifyArgs(method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)I"))
    private void modifyDrawTextWithShadow(Args args) {
        if (getInstance().getAdjustChat())
        {
            args.set(2, Letterboxing.verticalBarWidth);
            args.set(3, Letterboxing.horizontalBarHeight + 1);
        }
    }

    //Unfocused Chat
    @Dynamic
    @ModifyArg(method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V"),
            index = 0)
    private int modifyUnfocusedChat(int x1) {
        if (getInstance().getAdjustChat())
        {
            return x1 + Letterboxing.verticalBarWidth;
        } else
        {
            return x1;
        }
    }

    //Focused Chat
    @Dynamic
    @ModifyArg(method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIIII)V"),
            index = 0)
    private int modifyFocusedChat(int x1) {
        if (getInstance().getAdjustChat())
        {
            return x1 + Letterboxing.verticalBarWidth;
        } else
        {
            return x1;
        }
    }

    // Variable d
    @Dynamic
    @ModifyVariable(at = @At("HEAD"), method = "mouseClicked", ordinal = 0)
    private double modifyD(double d) {
        if (getInstance().getAdjustChat())
        {
            return d + Letterboxing.verticalBarWidth;
        } else
        {
            return d;
        }
    }

    // Variable e
    @Dynamic
    @ModifyVariable(at = @At("HEAD"), method = "mouseClicked", ordinal = 1)
    private double modifyE(double e) {
        if (getInstance().getAdjustChat())
        {
            return e - Letterboxing.horizontalBarHeight;
        } else
        {
            return e;
        }
    }

    // Modifies 4.0 in return statement
    @Dynamic
    @Inject(method = "toChatLineX", at = @At("RETURN"), cancellable = true)
    private void injectWidth(CallbackInfoReturnable<Double> cir) {
        if (getInstance().getAdjustChat())
        {
            cir.setReturnValue(cir.getReturnValueD() - (double) Letterboxing.verticalBarWidth);
        } else
        {
            cir.setReturnValue(cir.getReturnValueD());
        }
    }

    @Dynamic
    @ModifyExpressionValue(method = "toChatLineY", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;getScaledHeight()I"))
    private int modifyToChatLineY(int value) {
        if (getInstance().getAdjustChat())
        {
            return client.getWindow().getScaledHeight() - Letterboxing.horizontalBarHeight;
        } else
        {
            return value;
        }
    }
}