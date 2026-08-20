package com.copythenerd.letterboxing.mixin;

import com.copythenerd.letterboxing.Letterboxing;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import static com.copythenerd.letterboxing.config.LetterboxingConfig.getInstance;

@Mixin(ChatInputSuggestor.class)
public abstract class ChatInputSuggestorMixin {
    //Unfocused Chat
    @Dynamic
    @ModifyArg(method = "show",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ChatInputSuggestor$SuggestionWindow;<init>(Lnet/minecraft/client/gui/screen/ChatInputSuggestor;IIILjava/util/List;Z)V"),
            index = 2)
    private int modifyUnfocusedChat(int k) {
        if (getInstance().getAdjustChat())
        {
            return k - Letterboxing.horizontalBarHeight;
        } else
        {
            return k;
        }
    }

    // Modify message-position (of e.g. "unknown or incomplete command")
    @Dynamic
    @ModifyArgs(method = "renderMessages",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V"))
    private void modifyFill(Args args) {
        if (getInstance().getAdjustChat())
        {
            int y1 = (int) args.get(1) - Letterboxing.horizontalBarHeight;
            int y2 = (int) args.get(3) - Letterboxing.horizontalBarHeight;

            args.set(1, y1);
            args.set(3, y2);
        }
    }

    @Dynamic
    @ModifyArg(method = "renderMessages",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;III)V"),
            index = 3)
    private int modifyDrawTextWithShadowOrdered(int y) {
        if (getInstance().getAdjustChat())
        {
            return y - Letterboxing.horizontalBarHeight;
        } else
        {
            return y;
        }
    }
}