package com.copythenerd.letterboxing.mixin;

import com.copythenerd.letterboxing.Letterboxing;
import net.minecraft.client.gui.hud.bar.ExperienceBar;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static com.copythenerd.letterboxing.config.LetterboxingConfig.getInstance;

@Mixin(ExperienceBar.class)
public class ExperienceBarMixin {

    // Modify j (height of experience bar)
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
