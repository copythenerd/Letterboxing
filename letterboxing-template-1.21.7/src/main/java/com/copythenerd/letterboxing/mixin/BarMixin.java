package com.copythenerd.letterboxing.mixin;

import com.copythenerd.letterboxing.Letterboxing;
import net.minecraft.client.gui.hud.bar.Bar;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static com.copythenerd.letterboxing.config.LetterboxingConfig.getInstance;

@Mixin(Bar.class)
public interface BarMixin {

    // Modify j (height of experience level)
    @Dynamic
    @ModifyVariable(at = @At("STORE"), method = "drawExperienceLevel", ordinal = 2)
    private static int modifyI(int j) {
        if (getInstance().getAdjustPlayerHud())
        {
            return j - Letterboxing.horizontalBarHeight;
        } else
        {
            return j;
        }
    }
}
