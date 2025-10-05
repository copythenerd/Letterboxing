package com.copythenerd.letterboxing.mixin;

import com.copythenerd.letterboxing.Letterboxing;
import net.minecraft.client.gui.hud.bar.JumpBar;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static com.copythenerd.letterboxing.config.LetterboxingConfig.getInstance;

@Mixin(JumpBar.class)
public class JumpBarMixin {

    // Modify j (height of experience level)
    @Dynamic
    @ModifyVariable(at = @At("STORE"), method = "renderBar", ordinal = 1)
    private int modifyI(int j) {
        if (getInstance().getAdjustPlayerHud())
        {
            return j - Letterboxing.horizontalBarHeight;
        } else
        {
            return j;
        }
    }
}
