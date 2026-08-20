package com.copythenerd.letterboxing.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.LayeredDrawer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import static com.copythenerd.letterboxing.Letterboxing.*;
import static com.copythenerd.letterboxing.config.LetterboxingConfig.getInstance;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow
    @Final
    private LayeredDrawer layeredDrawer;
    // Use client instead of context, because context.getScaledWindowHeight calls client.getWindow().getScaledHeight()
    @Unique
    MinecraftClient client = MinecraftClient.getInstance();

    // Status Effect Overlay (on the right of the screen)
    @Dynamic
    @ModifyExpressionValue(method = "renderStatusEffectOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowWidth()I"))
    private int modifyEffectBackgroundAmbientTexture(int value) {
        if (getInstance().getAdjustPlayerHud())
        {
            // This now subtracts from the getScaledWindowHeight()-call
            return client.getWindow().getScaledWidth() - verticalBarWidth;
        } else
        {
            return value;
        }
    }

    // Variable l for status effect overlay height
    @Dynamic
    @ModifyVariable(at = @At("STORE"), method = "renderStatusEffectOverlay", ordinal = 3)
    private int modifyL(int l) {
        if (getInstance().getAdjustPlayerHud())
        {
            return l + horizontalBarHeight;
        } else
        {
            return l;
        }
    }

    // Hotbar texture
    @Dynamic
    @ModifyArg(method = "renderHotbar",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Ljava/util/function/Function;Lnet/minecraft/util/Identifier;IIII)V",
                    ordinal = 0), index = 3)
    private int hotbarTexture(int value) {
        if (getInstance().getAdjustPlayerHud())
        {
            // In the Game Code, it is "- 22"
            return client.getWindow().getScaledHeight() - 22 - horizontalBarHeight;
        } else
        {
            return value;
        }
    }

    // Hotbar selection texture
    @Dynamic
    @ModifyArg(method = "renderHotbar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Ljava/util/function/Function;Lnet/minecraft/util/Identifier;IIII)V",
                    ordinal = 1), index = 3)
    private int hotbarSelectionTexture(int value) {
        if (getInstance().getAdjustPlayerHud())
        {
            return client.getWindow().getScaledHeight() - 22 - 1 - horizontalBarHeight;
        } else
        {
            return value;
        }
    }

    // Offhand Left
    @Dynamic
    @ModifyArg(method = "renderHotbar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Ljava/util/function/Function;Lnet/minecraft/util/Identifier;IIII)V",
                    ordinal = 2), index = 3)
    private int offhandLeft(int value) {
        if (getInstance().getAdjustPlayerHud())
        {
            return client.getWindow().getScaledHeight() - 23 - horizontalBarHeight;
        } else
        {
            return value;
        }
    }

    // Offhand Right
    @Dynamic
    @ModifyArg(method = "renderHotbar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Ljava/util/function/Function;Lnet/minecraft/util/Identifier;IIII)V",
                    ordinal = 3), index = 3)
    private int offhandRight(int value) {
        if (getInstance().getAdjustPlayerHud())
        {
            return client.getWindow().getScaledHeight() - 23 - horizontalBarHeight;
        } else
        {
            return value;
        }
    }

    // Variable o in for-loop
    @Dynamic
    @ModifyExpressionValue(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowHeight()I"), slice = @Slice(
            from = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V")
    ))
    private int modifyO(int value) {
        if (getInstance().getAdjustPlayerHud())
        {
            return client.getWindow().getScaledHeight() - horizontalBarHeight;
        } else
        {
            return value;
        }
    }

    // Variable m
    @Dynamic
    @ModifyExpressionValue(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowHeight()I"), slice = @Slice(
            from = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbarItem(Lnet/minecraft/client/gui/DrawContext;" +
                    "IILnet/minecraft/client/render/RenderTickCounter;Lnet/minecraft/entity/player/PlayerEntity;" +
                    "Lnet/minecraft/item/ItemStack;I)V")
    ))
    private int modifyScaledHeight(int value) {
        if (getInstance().getAdjustPlayerHud())
        {
            return client.getWindow().getScaledHeight() - horizontalBarHeight;
        } else
        {
            return value;
        }
    }

    // Variable n for hotbar attack indicator
    @Dynamic
    @ModifyExpressionValue(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowHeight()I"), slice = @Slice(
            from = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getAttackCooldownProgress(F)F")
    ))
    private int modifyAttackIndicatorBackgroundTexture(int value) {
        if (getInstance().getAdjustPlayerHud())
        {
            return client.getWindow().getScaledHeight() - horizontalBarHeight;
        } else
        {
            return value;
        }
    }

    // Variable l inside if-statement-body
    @Dynamic
    @ModifyExpressionValue(method = "renderExperienceBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowHeight()I"), slice = @Slice(
            from = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getAttackCooldownProgress(F)F")
    ))
    private int modifyExperienceBar(int value) {
        if (getInstance().getAdjustPlayerHud())
        {
            return value - horizontalBarHeight;
        } else
        {
            return value;
        }
    }

    // Variable k inside if-statement-body
    @Dynamic
    @ModifyExpressionValue(method = "renderExperienceLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowHeight()I", ordinal = 0))
    private int modifyExperienceLevel(int value) {
        if (getInstance().getAdjustPlayerHud())
        {
            return value - horizontalBarHeight;
        } else
        {
            return value;
        }
    }

    // Value k
    @Dynamic
    @ModifyExpressionValue(method = "renderMountJumpBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowHeight()I"))
    private int modifyMountJumpBar(int value) {
        if (getInstance().getAdjustPlayerHud())
        {
            return value - horizontalBarHeight;
        } else
        {
            return value;
        }
    }


    // Value m
    @Dynamic
    @ModifyExpressionValue(method = "renderMountHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowHeight()I"))
    private int modifyrenderMountHealth(int value) {
        if (getInstance().getAdjustPlayerHud())
        {
            return value - horizontalBarHeight;
        } else
        {
            return value;
        }
    }

    // Value k inside if-statement-body
    @Dynamic
    @ModifyExpressionValue(method = "renderHeldItemTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowHeight()I"))
    private int modifyHeldItemTooltip(int value) {
        if (getInstance().getAdjustPlayerHud())
        {
            return value - horizontalBarHeight;
        } else
        {
            return value;
        }
    }

    // Value n
    @Dynamic
    @ModifyExpressionValue(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowHeight()I"))
    private int modifyStatusBars(int value) {
        if (getInstance().getAdjustPlayerHud())
        {
            return value - horizontalBarHeight;
        } else
        {
            return value;
        }
    }

    // Technically useless, but for the sake of completeness I will also move this one
    // Value k
    @Dynamic
    @ModifyArgs(method = "renderDemoTimer",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithBackground(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIII)I"))
    private void modifyDemoTimer(Args args) {
        if (getInstance().getAdjustPlayerHud())
        {
            int widthValue = args.get(2);
            int heightValue = args.get(3);
            args.set(2, widthValue - verticalBarWidth);
            args.set(3, heightValue + horizontalBarHeight);
        }
    }

    // Overlays (e.g. Powder Snow)
    @Dynamic
    @ModifyArgs(method = "renderOverlay",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Ljava/util/function/Function;Lnet/minecraft/util/Identifier;IIFFIIIII)V"))
    private void modifyRenderOverlay(Args args) {
        if (getInstance().getAdjustPlayerHud())
        {
            args.set(2, verticalBarWidth);
            args.set(3, horizontalBarHeight);
            args.set(6, client.getWindow().getScaledWidth() - 2 * verticalBarWidth);
            args.set(7, client.getWindow().getScaledHeight() - 2 * horizontalBarHeight);
            args.set(8, client.getWindow().getScaledWidth() - 2 * verticalBarWidth);
            args.set(9, client.getWindow().getScaledHeight() - 2 * horizontalBarHeight);
        }
    }

    // Vignette Overlay
    @Dynamic
    @ModifyArgs(method = "renderVignetteOverlay",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Ljava/util/function/Function;Lnet/minecraft/util/Identifier;IIFFIIIII)V"))
    private void modifyRenderVignetteOverlay(Args args) {
        if (getInstance().getAdjustPlayerHud())
        {
            args.set(2, verticalBarWidth);
            args.set(3, horizontalBarHeight);
            args.set(6, client.getWindow().getScaledWidth() - 2 * verticalBarWidth);
            args.set(7, client.getWindow().getScaledHeight() - 2 * horizontalBarHeight);
            args.set(8, client.getWindow().getScaledWidth() - 2 * verticalBarWidth);
            args.set(9, client.getWindow().getScaledHeight() - 2 * horizontalBarHeight);
        }
    }

    // Portal Overlay
    @Dynamic
    @ModifyArgs(method = "renderPortalOverlay",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawSpriteStretched(Ljava/util/function/Function;Lnet/minecraft/client/texture/Sprite;IIIII)V"))
    private void modifyRenderPortalOverlay(Args args) {
        if (getInstance().getAdjustPlayerHud())
        {
            args.set(2, verticalBarWidth);
            args.set(3, horizontalBarHeight);
            args.set(4, client.getWindow().getScaledWidth() - 2 * verticalBarWidth);
            args.set(5, client.getWindow().getScaledHeight() - 2 * horizontalBarHeight);
        }
    }

    // Change scaled window width to drawTexture()
    @Dynamic
    @ModifyExpressionValue(method = "renderSpyglassOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowWidth()I"),
            slice = @Slice(
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;floor(F)I")
            ))
    private int modifySpyglassScaledWidthToDrawTexture(int value) {
        if (getInstance().getAdjustPlayerHud())
        {
            return client.getWindow().getScaledWidth() - 2 * verticalBarWidth;
        } else
        {
            return value;
        }
    }

    // Change scaled window height to drawTexture()
    @Dynamic
    @ModifyExpressionValue(method = "renderSpyglassOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowHeight()I"),
            slice = @Slice(
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;floor(F)I")
            ))
    private int modifySpyglassScaledHeightToDrawTexture(int value) {
        if (getInstance().getAdjustPlayerHud())
        {
            return client.getWindow().getScaledHeight() - 2 * horizontalBarHeight;
        } else
        {
            return value;
        }
    }

    // Change scaled window width from drawTexture()
    @Dynamic
    @ModifyExpressionValue(method = "renderSpyglassOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowWidth()I"),
            slice = @Slice(
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;floor(F)I")
            ))
    private int modifySpyglassScaledWidthToDrawTextureB(int value) {
        if (getInstance().getAdjustPlayerHud())
        {
            return client.getWindow().getScaledWidth() - verticalBarWidth;
        } else
        {
            return value;
        }
    }

    // Change scaled window height from drawTexture()
    @Dynamic
    @ModifyExpressionValue(method = "renderSpyglassOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowHeight()I"),
            slice = @Slice(
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;floor(F)I")
            ))
    private int modifySpyglassScaledHeightToDrawTextureB(int value) {
        if (getInstance().getAdjustPlayerHud())
        {
            return client.getWindow().getScaledHeight() - horizontalBarHeight;
        } else
        {
            return value;
        }
    }

    // First Fill (bottom)
    @Dynamic
    @ModifyArgs(method = "renderSpyglassOverlay",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(Lnet/minecraft/client/render/RenderLayer;IIIIII)V", ordinal = 0))
    private void modifyFill(Args args) {
        if (getInstance().getAdjustPlayerHud())
        {
            args.set(1, verticalBarWidth);
            args.set(3, client.getWindow().getScaledWidth() - verticalBarWidth);
            args.set(4, client.getWindow().getScaledHeight());
        }
    }

    // Second Fill (top)
    @Dynamic
    @ModifyArgs(method = "renderSpyglassOverlay",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(Lnet/minecraft/client/render/RenderLayer;IIIIII)V", ordinal = 1))
    private void modifyFill2(Args args) {
        if (getInstance().getAdjustPlayerHud())
        {
            args.set(1, verticalBarWidth);
            args.set(2, horizontalBarHeight);
            args.set(3, client.getWindow().getScaledWidth() - verticalBarWidth);
        }
    }

    // Third Fill (left)
    @Dynamic
    @ModifyArgs(method = "renderSpyglassOverlay",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(Lnet/minecraft/client/render/RenderLayer;IIIIII)V", ordinal = 2))
    private void modifyFill3(Args args) {
        if (getInstance().getAdjustPlayerHud())
        {
            args.set(1, verticalBarWidth);
        }
    }


    // Fourth Fill (right)
    @Dynamic
    @ModifyArgs(method = "renderSpyglassOverlay",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(Lnet/minecraft/client/render/RenderLayer;IIIIII)V", ordinal = 3))
    private void modifyFillRight(Args args) {
        if (getInstance().getAdjustPlayerHud())
        {
            args.set(3, client.getWindow().getScaledWidth() - verticalBarWidth);
        }
    }

    // Nausea Overlay Window width
    @Dynamic
    @ModifyExpressionValue(method = "renderNauseaOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowWidth()I"))
    private int modifyNauseaOverlayWidth(int value) {
        if (getInstance().getAdjustPlayerHud())
        {
            return client.getWindow().getScaledWidth() - 2 * verticalBarWidth;
        } else
        {
            return value;
        }
    }

    // Nausea Overlay Window height
    @Dynamic
    @ModifyExpressionValue(method = "renderNauseaOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowHeight()I"))
    private int modifyNauseaOverlayHeight(int value) {
        if (getInstance().getAdjustPlayerHud())
        {
            return client.getWindow().getScaledHeight() - 2 * horizontalBarHeight;
        } else
        {
            return value;
        }
    }

    // Nausea Overlay Texture Position
    @Dynamic
    @ModifyArgs(method = "renderNauseaOverlay",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Ljava/util/function/Function;Lnet/minecraft/util/Identifier;IIFFIIIII)V"))
    private void modifyNauseaOverlayTexturePosition(Args args) {
        if (getInstance().getAdjustPlayerHud())
        {
            args.set(2, verticalBarWidth);
            args.set(3, horizontalBarHeight);
        }
    }

    // Sleep Overlay
    @Dynamic
    @ModifyArgs(method = "renderSleepOverlay",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(Lnet/minecraft/client/render/RenderLayer;IIIII)V"))
    private void modifySleepOverlay(Args args) {
        if (getInstance().getAdjustPlayerHud())
        {
            args.set(1, verticalBarWidth);
            args.set(2, horizontalBarHeight);
            args.set(3, client.getWindow().getScaledWidth() - 2 * verticalBarWidth);
            args.set(4, client.getWindow().getScaledHeight() - 2 * horizontalBarHeight);
        }
    }

    // Overlay Message
    @Dynamic
    @ModifyArgs(method = "renderOverlayMessage",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithBackground(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIII)I"))
    private void modifyMessageOverlayDrawTextWithBackground(Args args) {
        if (getInstance().getAdjustPlayerHud())
        {
            args.set(3, -4 - horizontalBarHeight);
        }
    }

    // Player List
    @Dynamic
    @ModifyArgs(method = "renderPlayerList",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/PlayerListHud;render(Lnet/minecraft/client/gui/DrawContext;ILnet/minecraft/scoreboard/Scoreboard;Lnet/minecraft/scoreboard/ScoreboardObjective;)V"))
    private void modifyPlayerList(Args args) {
        if (getInstance().getAdjustPlayerHud())
        {
            args.set(1, client.getWindow().getScaledWidth() - verticalBarWidth);
        }
    }

    // Scoreboard Sidebar Height
    @Dynamic
    @ModifyExpressionValue(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowHeight()I"))
    private int modifyScoreBoardSidebarScaleHeight(int value) {
        if (getInstance().getAdjustPlayerHud())
        {
            return client.getWindow().getScaledHeight() - 2 * horizontalBarHeight;
        } else
        {
            return value;
        }
    }

    // Scoreboard Sidebar Width
    @Dynamic
    @ModifyExpressionValue(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowWidth()I"))
    private int modifyScoreBoardSidebarScaleWidth(int value) {
        if (getInstance().getAdjustPlayerHud())
        {
            return client.getWindow().getScaledWidth() - verticalBarWidth;
        } else
        {
            return value;
        }
    }

    @Dynamic
    @Inject(method = "<init>", at = @At("TAIL"))
    private void addLayer(MinecraftClient client, CallbackInfo ci) {
        this.layeredDrawer.addLayer(this::renderAboveHud);
    }

    @Unique
    private void renderAboveHud(DrawContext drawContext, RenderTickCounter renderTickCounter) {
        // Prevent bars from being rendered twice
        if (drawBars && !(client.currentScreen instanceof ChatScreen))
        {
            int windowHeight = client.getWindow().getScaledHeight();
            int windowWidth = client.getWindow().getScaledWidth();
            // Draw Lower Bar
            drawContext.fill(0, windowHeight, windowWidth, windowHeight - horizontalBarHeight, 0xFF000000);
            // Draw Upper Bar
            drawContext.fill(0, 0, windowWidth, horizontalBarHeight, 0xFF000000);

            // Draw Lower Bar
            drawContext.fill(0, windowHeight, verticalBarWidth, 0, 0xFF000000);

            // Draw Right Bar
            drawContext.fill(windowWidth - verticalBarWidth, 0, windowWidth, windowHeight, 0xFF000000);
        }
    }
}


