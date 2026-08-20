package com.copythenerd.letterboxing;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Items;
import net.minecraft.world.GameMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

import static com.copythenerd.letterboxing.config.LetterboxingConfig.getInstance;

public class Letterboxing implements ModInitializer {
    public static final String MOD_ID = "letterboxing";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // Current bar sizes
    public static int horizontalBarHeight = 0;
    public static int verticalBarWidth = 0;

    int windowHeight = 0;
    int windowWidth = 0;

    // Helper method to draw horizontal / vertical bars
    public static boolean drawBars = false;

    public static final Path pathToConfig = FabricLoader.getInstance().getConfigDirectory().toPath().resolve("letterboxing-config.json");

    @Override
    public void onInitialize() {
        /* Load Config */
        getInstance().load(pathToConfig);
        LOGGER.info("Loaded config file");


        HudRenderCallback.EVENT.register((drawContext, tickDeltaManager) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client != null)
            {
                windowHeight = client.getWindow().getScaledHeight();
                windowWidth = client.getWindow().getScaledWidth();

            }

            horizontalBarHeight = getInstance().getCurrentHorizontalHeight();
            verticalBarWidth = getInstance().getCurrentVerticalWidth();


            if (!client.options.hudHidden && client.interactionManager.getCurrentGameMode() != GameMode.SPECTATOR)
            {
                if (getInstance().getNormalVisibility())
                {
                    if (!client.player.getEquippedStack(EquipmentSlot.HEAD).isOf(Items.CARVED_PUMPKIN))
                    {
                        drawBars = true;
                    }
                    // Check, if player has carved pumpkin equipped and is in first person
                    else if (client.player.getEquippedStack(EquipmentSlot.HEAD).isOf(Items.CARVED_PUMPKIN) && client.options.getPerspective() == Perspective.FIRST_PERSON)
                    {
                        if (getInstance().getCarvedPumpkinVisibility())
                        {
                            drawBars = true;
                        }
                    }
                }

                // Check whether the player is wearing a carved pumpkin
                else if (client.player.getEquippedStack(EquipmentSlot.HEAD).isOf(Items.CARVED_PUMPKIN) && client.options.getPerspective() == Perspective.FIRST_PERSON)
                {
                    if (getInstance().getCarvedPumpkinVisibility())
                    {
                        drawBars = true;
                    }
                } else
                {
                    drawBars = false;
                    horizontalBarHeight = 0;
                    verticalBarWidth = 0;
                }
            } else if (client.interactionManager.getCurrentGameMode() == GameMode.SPECTATOR)
            {
                if (getInstance().getSpectatorVisibility())
                {
                    if (client.options.hudHidden)
                    {
                        if (getInstance().getF1Visibility())
                        {
                            drawBars = true;
                        }
                    } else
                    {
                        drawBars = true;
                    }
                } else if (!getInstance().getSpectatorVisibility())
                {
                    if (client.options.hudHidden)
                    {
                        if (getInstance().getF1Visibility())
                        {
                            drawBars = true;
                        }
                    } else
                    {
                        drawBars = false;
                        horizontalBarHeight = 0;
                        verticalBarWidth = 0;
                    }
                }
            }

            // Display bars in f1 mode, if flag is set
            else if (client.options.hudHidden && getInstance().getNormalVisibility())
            {
                drawBars = getInstance().getF1Visibility();
            } else if (client.options.hudHidden)
            {
                if (getInstance().getF1Visibility())
                {
                    drawBars = true;
                }
            } else
            {
                drawBars = false;
            }


        });
    }

    public static void saveConfig() {
        if (getInstance().save(pathToConfig))
        {
            LOGGER.info("Config saved successfully.");
        } else
        {
            LOGGER.error("Failed to save config.");
        }
    }
}