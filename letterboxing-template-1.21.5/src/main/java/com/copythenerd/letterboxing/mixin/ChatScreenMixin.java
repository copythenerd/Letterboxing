package com.copythenerd.letterboxing.mixin;

import com.copythenerd.letterboxing.Letterboxing;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.copythenerd.letterboxing.Letterboxing.*;
import static com.copythenerd.letterboxing.config.LetterboxingConfig.getInstance;


@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin {
    @Unique
    MinecraftClient client = MinecraftClient.getInstance();

    // Modifies first 4 in constructor TextFieldWidget (x coordinate left)
    @Dynamic
    @ModifyConstant(method = "init", constant = @Constant(intValue = 4, ordinal = 0))
    private int modifyFirst4(int value) {
        if (getInstance().getAdjustChat())
        {
            return 4 + Letterboxing.verticalBarWidth;
        } else
        {
            return value;
        }
    }

    // Modifies second 4 in constructor TextFieldWidget (x coordinate right)
    @ModifyConstant(method = "init", constant = @Constant(intValue = 4, ordinal = 1))
    private int modifySecond4(int value) {
        if (getInstance().getAdjustChat())
        {
            return 4 - Letterboxing.verticalBarWidth;
        } else
        {
            return value;
        }
    }

    // Modifies 12 in constructor TextFieldWidget (y coordinate bottom)
    @Dynamic
    @ModifyConstant(method = "init", constant = @Constant(intValue = 12, ordinal = 0))
    private int modify12(int value) {
        if (getInstance().getAdjustChat())
        {
            return 12 + Letterboxing.horizontalBarHeight;
        } else
        {
            return value;
        }
    }

    // Modifies first 2 in context.fill() (x coordinate left)
    @Dynamic
    @ModifyConstant(method = "render", constant = @Constant(intValue = 2, ordinal = 0))
    private int modifyFirst2(int value) {
        if (getInstance().getAdjustChat())
        {
            return 2 + Letterboxing.verticalBarWidth;
        } else
        {
            return value;
        }
    }

    // Modifies second 2 in context.fill() (x coordinate right)
    @Dynamic
    @ModifyConstant(method = "render", constant = @Constant(intValue = 2, ordinal = 1))
    private int modifySecond2(int value) {
        if (getInstance().getAdjustChat())
        {
            return 2 + Letterboxing.verticalBarWidth;
        } else
        {
            return value;
        }
    }

    // Modifies third 2 in context.fill() (y coordinate bottom)
    @Dynamic
    @ModifyConstant(method = "render", constant = @Constant(intValue = 2, ordinal = 2))
    private int modifyThird2(int value) {
        if (getInstance().getAdjustChat())
        {
            return 2 + Letterboxing.horizontalBarHeight;
        } else
        {
            return value;
        }
    }

    // Modifies 14 in context.fill() (y coordinate bottom
    @Dynamic
    @ModifyConstant(method = "render", constant = @Constant(intValue = 14, ordinal = 0))
    private int modify14(int value) {
        if (getInstance().getAdjustChat())
        {
            return 14 + Letterboxing.horizontalBarHeight;
        } else
        {
            return value;
        }
    }

    @Dynamic
    @Inject(method = "render", at = @At("TAIL"))
    public void renderAboveChat(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (drawBars && client.currentScreen instanceof ChatScreen)
        {
            int windowHeight = client.getWindow().getScaledHeight();
            int windowWidth = client.getWindow().getScaledWidth();
            // Draw Lower Bar
            context.fill(0, windowHeight, windowWidth, windowHeight - horizontalBarHeight, 0xFF000000);
            // Draw Upper Bar
            context.fill(0, 0, windowWidth, horizontalBarHeight, 0xFF000000);

            // Draw Lower Bar
            context.fill(0, windowHeight, verticalBarWidth, 0, 0xFF000000);

            // Draw Right Bar
            context.fill(windowWidth - verticalBarWidth, 0, windowWidth, windowHeight, 0xFF000000);
        }
    }
}