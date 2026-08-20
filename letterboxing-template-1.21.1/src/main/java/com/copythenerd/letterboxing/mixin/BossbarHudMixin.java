package com.copythenerd.letterboxing.mixin;

import com.copythenerd.letterboxing.Letterboxing;
import net.minecraft.client.gui.hud.BossBarHud;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static com.copythenerd.letterboxing.config.LetterboxingConfig.getInstance;

@Mixin(BossBarHud.class)
public abstract class BossbarHudMixin {
    // Ordinal 1 refers to j (i has ordinal 0); ordinals refer to variables of the same type (here int)
    @Dynamic
    @ModifyVariable(at = @At("STORE"), method = "render", ordinal = 1)
    private int injected(int value) {
        if (getInstance().getAdjustBossbar())
        {
            // 12 is the actual value
            return 12 + Letterboxing.horizontalBarHeight;
        } else
        {
            return value;
        }
    }
}