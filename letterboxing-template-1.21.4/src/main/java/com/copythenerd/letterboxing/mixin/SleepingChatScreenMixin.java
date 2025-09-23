package com.copythenerd.letterboxing.mixin;

import com.copythenerd.letterboxing.Letterboxing;
import net.minecraft.client.gui.screen.SleepingChatScreen;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static com.copythenerd.letterboxing.config.LetterboxingConfig.getInstance;

@Mixin(SleepingChatScreen.class)
public abstract class SleepingChatScreenMixin {

    //Stop sleeping button
    @Dynamic
    @ModifyArg(method = "init",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;dimensions(IIII)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;"),
            index = 1)
    private int modifyStopSleepingButton(int y) {
        if (getInstance().getAdjustPlayerHud())
        {
            return y - Letterboxing.horizontalBarHeight;
        } else
        {
            return y;
        }
    }
}