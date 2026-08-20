package com.copythenerd.letterboxing.mixin;

import com.copythenerd.letterboxing.Letterboxing;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.copythenerd.letterboxing.config.LetterboxingConfig.getInstance;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin {

    @Unique
    MinecraftClient client = MinecraftClient.getInstance();

    // Value j
    @Dynamic
    @ModifyExpressionValue(method = "Lnet/minecraft/client/gui/hud/ChatHud;render(Lnet/minecraft/client/gui/hud/ChatHud$Backend;IIZ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;ceil(F)I"))
    private int modifyK(int value) {
        if (getInstance().getAdjustChat())
        {
            return value + Letterboxing.verticalBarWidth;
        } else
        {
            return value;
        }
    }

    // Modifies the y-offset of the chat message
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

    // Modifies the x-offset of the chat message
    @Inject(
            method = "render(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/font/TextRenderer;IIIZZ)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/ChatHud;render(Lnet/minecraft/client/gui/hud/ChatHud$Backend;IIZ)V"
            )
    )
    private void shiftChatHud(DrawContext context, TextRenderer textRenderer, int currentTick, int mouseX, int mouseY, boolean interactable, boolean bl, CallbackInfo ci) {
        if (getInstance().getAdjustChat()) {
            // Shift the entire chat horizontally.
            // Because this runs inside pushMatrix(), it won't affect other HUD elements.
            context.getMatrices().translate(Letterboxing.verticalBarWidth, 0);
        }
    }
}

