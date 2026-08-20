package com.copythenerd.letterboxing.config;

import com.copythenerd.letterboxing.Letterboxing;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import static com.copythenerd.letterboxing.config.LetterboxingConfig.getInstance;

@Environment(EnvType.CLIENT)
public class LetterboxingConfigScreen extends Screen {
    private final Screen parent;
    private final LetterboxingConfig config;

    public LetterboxingConfigScreen(Screen parent, LetterboxingConfig config) {
        super(Text.translatable("config.letterboxing.title.text"));
        this.config = config;
        this.parent = parent;
    }

    int x = 0;
    int y = 0;

    //Button / Slider Widgets
    public ButtonWidget format;
    public ButtonWidget normal;
    public ButtonWidget spectator;
    public ButtonWidget f1;
    public ButtonWidget carvedPumpkin;

    public ButtonWidget bossbar;
    public ButtonWidget chat;
    public ButtonWidget debug;
    public ButtonWidget playerHud;
    public ButtonWidget toasts;
    public ButtonWidget subtitles;

    public ButtonWidget done;
    SliderWidget customHeightSlider;
    SliderWidget customWidthSlider;

    double widthSliderValue = ((double) getInstance().getCustomVerticalWidth() / 300.0);
    double heightSliderValue = ((double) getInstance().getCustomHorizontalHeight() / 300.0);

    public static MinecraftClient client = MinecraftClient.getInstance();
    // Use an array to make its first entry effectively final
    private static final ConfigAspectRatio[] aspectRatios = ConfigAspectRatio.values();


    // Helper Methode to calculate the bar sizes from current aspect ratio
    public static void updateBarSize() {
        float width = getInstance().getCurrentRatio().getWidth();
        float height = getInstance().getCurrentRatio().getHeight();

        float ratio = width / height;
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();
        float currentRatio = (float) screenWidth / (float) screenHeight;

        if (ratio < currentRatio)
        {
            // Use pillarboxing
            getInstance().setCurrentVerticalWidth((int) ((float) (screenWidth - (int) ((width / height) * screenHeight))) / 2);
            getInstance().setCurrentHorizontalHeight(0);

        } else
        {
            // Use letterboxing
            getInstance().setCurrentVerticalWidth(0);
            getInstance().setCurrentHorizontalHeight((int) ((float) (screenHeight - (int) ((height / width) * screenWidth)) / 2));
        }
    }

    // Helper method to calculate aspect ratio of current screen-size
    private String getRatio(int height, int width) {
        float ratio = (float) width / (float) height;
        if (ratio < 1)
        {
            return "1:" + String.format("%.2f", 1.0F / ratio);
        } else
        {
            return String.format("%.2f", ratio) + ":1";
        }
    }


    private Text getButtonBooleanState(Boolean state) {
        if (state)
        {
            return Text.translatable("config.letterboxing.on.buttontext");
        } else
        {
            return Text.translatable("config.letterboxing.off.buttontext");
        }
    }

    @Override
    protected void init() {


        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null)
        {
            int windowHeight = client.getWindow().getScaledHeight();

            x = client.getWindow().getScaledWidth();
            y = windowHeight;
        }

        ConfigAspectRatio.NONE.setText(Text.literal(getRatio(y, x)).append(" ").append(Text.translatable("config.letterboxing.none.buttontext")));
        ConfigAspectRatio.CUSTOM.setText(Text.literal(getRatio(getInstance().getCustomHorizontalHeight(), getInstance().getCustomVerticalWidth())).append(" ").append(Text.translatable("config.letterboxing.custom.buttontext")));

        // Aspect ratio button
        format = ButtonWidget.builder(Text.translatable("config.letterboxing.aspectratio.buttontext").append(" : ").append(aspectRatios[getInstance().getCurrentIndex()].getText()), // Use the Text object directly
                        (widget) -> {

                            // Change to next state
                            getInstance().nextIndex();

                            // set current ratio
                            getInstance().setCurrentRatio(aspectRatios[getInstance().getCurrentIndex()]);

                            if (getInstance().getCurrentRatio() == ConfigAspectRatio.NONE)
                            {
                                getInstance().setCurrentHorizontalHeight(0);
                                getInstance().setCurrentVerticalWidth(0);
                                ConfigAspectRatio.NONE.setText(Text.literal(getRatio(y, x)).append(" ").append(Text.translatable("config.letterboxing.none.buttontext")));
                            } else if (getInstance().getCurrentRatio() == ConfigAspectRatio.CUSTOM)
                            {
                                getInstance().setCurrentHorizontalHeight(getInstance().getCustomHorizontalHeight());
                                getInstance().setCurrentVerticalWidth(getInstance().getCustomVerticalWidth());
                                ConfigAspectRatio.CUSTOM.setText(Text.literal(getRatio(getInstance().getCustomHorizontalHeight(), getInstance().getCustomVerticalWidth())).append(" ").append(Text.translatable("config.letterboxing.custom.buttontext")));
                                updateBarSize();
                            } else
                            {
                                updateBarSize();
                            }

                            // Display button tooltip

                            // Refresh the screen
                            this.refreshWidgetPositions();
                            this.refreshWidgetPositions();

                            // Update the button text using the actual Text object, not its toString()
                            widget.setMessage(aspectRatios[getInstance().getCurrentIndex()].getText());
                        })
                .dimensions(x / 2 - 150, y / 2 - 90, 300, 20)
                .tooltip(Tooltip.of(Text.translatable("config.letterboxing.aspectratio.tooltip")))
                .build();
        // ((double) getInstance().getCustomVerticalWidth() / 300.0)
        customWidthSlider = new SliderWidget(x / 2 - 150, y / 2 - 65, 145, 20, Text.empty(), widthSliderValue) {

            @Override
            protected void applyValue() {
                getInstance().setCustomVerticalWidth(MathHelper.floor(MathHelper.clampedLerp(1.0, 300.0, this.value)));
                ConfigAspectRatio.CUSTOM.setWidth(getInstance().getCustomVerticalWidth());
                if (getInstance().getCurrentRatio() == ConfigAspectRatio.CUSTOM)
                {
                    updateBarSize();
                }

                // Update text of format-button
                ConfigAspectRatio.CUSTOM.setText(Text.literal(getRatio(getInstance().getCustomHorizontalHeight(), getInstance().getCustomVerticalWidth())).append(" (Custom)"));
                format.setMessage(aspectRatios[getInstance().getCurrentIndex()].getText());
            }

            {
                this.updateMessage();
                widthSliderValue = ((double) getInstance().getCustomVerticalWidth() / 300.0);
            }

            @Override
            protected void updateMessage() {
                this.setMessage(Text.translatable("config.letterboxing.width.slidertext").append(" : ").append(String.valueOf(getInstance().getCustomVerticalWidth())));
            }
        };

        customHeightSlider = new SliderWidget(x / 2 + 5, y / 2 - 65, 145, 20, Text.empty(), heightSliderValue) {

            @Override
            protected void applyValue() {
                getInstance().setCustomHorizontalHeight(MathHelper.floor(MathHelper.clampedLerp(1.0, 300.0, this.value)));
                ConfigAspectRatio.CUSTOM.setHeight(getInstance().getCustomHorizontalHeight());
                if (getInstance().getCurrentRatio() == ConfigAspectRatio.CUSTOM)
                {
                    updateBarSize();
                }

                ConfigAspectRatio.CUSTOM.setText(Text.literal(getRatio(getInstance().getCustomHorizontalHeight(), getInstance().getCustomVerticalWidth())).append(" (Custom)"));
                format.setMessage(aspectRatios[getInstance().getCurrentIndex()].getText());

            }

            {
                this.updateMessage();
                heightSliderValue = ((double) getInstance().getCustomHorizontalHeight() / 300.0);
            }

            @Override
            protected void updateMessage() {
                this.setMessage(Text.translatable("config.letterboxing.height.slidertext").append(" : ").append(String.valueOf(getInstance().getCustomHorizontalHeight())));
            }
        };

        normal = ButtonWidget.builder(Text.translatable("config.letterboxing.visible.buttontext").append(" ").append(Text.translatable("config.letterboxing.normal.buttontext").append(": ").append(getButtonBooleanState(getInstance().getNormalVisibility()))),
                        (widget) -> {
                            getInstance().setNormalVisibility(!getInstance().getNormalVisibility());

                            // Refresh the screen (call twice to prevent slider-widgets from making a "leap" after their values have been changed
                            this.refreshWidgetPositions();
                            this.refreshWidgetPositions();
                        })
                .dimensions(x / 2 - 150, y / 2 - 40, 145, 20)
                .tooltip(Tooltip.of(Text.translatable("config.letterboxing.normal.tooltip")))
                .build();

        spectator = ButtonWidget.builder(Text.translatable("config.letterboxing.visible.buttontext").append(" ").append(Text.translatable("config.letterboxing.spectator.buttontext").append(": ").append(getButtonBooleanState(getInstance().getSpectatorVisibility()))),
                        (widget) -> {
                            getInstance().setSpectatorVisibility(!getInstance().getSpectatorVisibility());
                            // Refresh the screen
                            this.refreshWidgetPositions();
                            this.refreshWidgetPositions();

                        })
                .dimensions(x / 2 + 5, y / 2 - 40, 145, 20)
                .tooltip(Tooltip.of(Text.translatable("config.letterboxing.spectator.tooltip")))
                .build();

        f1 = ButtonWidget.builder(Text.translatable("config.letterboxing.visible.buttontext").append(" ").append(Text.translatable("config.letterboxing.f1.buttontext").append(": ").append(getButtonBooleanState(getInstance().getF1Visibility()))),
                        (widget) -> {
                            getInstance().setF1Visibility(!getInstance().getF1Visibility());
                            // Refresh the screen
                            this.refreshWidgetPositions();
                            this.refreshWidgetPositions();

                        })
                .dimensions(x / 2 - 150, y / 2 - 15, 145, 20)
                .tooltip(Tooltip.of(Text.translatable("config.letterboxing.f1.tooltip")))
                .build();

        carvedPumpkin = ButtonWidget.builder(Text.translatable("config.letterboxing.carvedpumpkin.buttontext").append(": ").append(getButtonBooleanState(getInstance().getCarvedPumpkinVisibility())),
                        (widget) -> {
                            getInstance().setCarvedPumpkinVisibility(!getInstance().getCarvedPumpkinVisibility());
                            // Refresh the screen
                            this.refreshWidgetPositions();
                            this.refreshWidgetPositions();
                        })
                .dimensions(x / 2 + 5, y / 2 - 15, 145, 20)
                .tooltip(Tooltip.of(Text.translatable("config.letterboxing.carvedpumpkin.tooltip")))
                .build();

        bossbar = ButtonWidget.builder(Text.translatable("config.letterboxing.bossbarhud.buttontext").append(": ").append(getButtonBooleanState(getInstance().getAdjustBossbar())),
                        (widget) -> {
                            getInstance().setAdjustBossbar(!getInstance().getAdjustBossbar());
                            // Refresh the screen
                            this.refreshWidgetPositions();
                            this.refreshWidgetPositions();

                        })
                .dimensions(x / 2 - 150, y / 2 + 10, 145, 20)
                .tooltip(Tooltip.of(Text.translatable("config.letterboxing.bossbarhud.tooltip").append("\n").append(Text.translatable("config.letterboxing.warning.tooltip"))))
                .build();

        chat = ButtonWidget.builder(Text.translatable("config.letterboxing.chat.buttontext").append(": ").append(getButtonBooleanState(getInstance().getAdjustChat())),
                        (widget) -> {
                            getInstance().setAdjustChat(!getInstance().getAdjustChat());
                            // Refresh the screen
                            this.refreshWidgetPositions();
                            this.refreshWidgetPositions();

                        })
                .dimensions(x / 2 + 5, y / 2 + 10, 145, 20)
                .tooltip(Tooltip.of(Text.translatable("config.letterboxing.chat.tooltip").append("\n").append(Text.translatable("config.letterboxing.warning.tooltip"))))
                .build();

        debug = ButtonWidget.builder(Text.translatable("config.letterboxing.debug.buttontext").append(": ").append(getButtonBooleanState(getInstance().getAdjustDebug())),
                        (widget) -> {
                            getInstance().setAdjustDebug(!getInstance().getAdjustDebug());
                            // Refresh the screen
                            this.refreshWidgetPositions();
                            this.refreshWidgetPositions();

                        })
                .dimensions(x / 2 - 150, y / 2 + 35, 145, 20)
                .tooltip(Tooltip.of(Text.translatable("config.letterboxing.debug.tooltip").append("\n").append(Text.translatable("config.letterboxing.warning.tooltip"))))
                .build();

        playerHud = ButtonWidget.builder(Text.translatable("config.letterboxing.playerhud.buttontext").append(": ").append(getButtonBooleanState(getInstance().getAdjustPlayerHud())),
                        (widget) -> {
                            getInstance().setAdjustPlayerHud(!getInstance().getAdjustPlayerHud());
                            // Refresh the screen
                            this.refreshWidgetPositions();
                            this.refreshWidgetPositions();

                        })
                .dimensions(x / 2 + 5, y / 2 + 35, 145, 20)
                .tooltip(Tooltip.of(Text.translatable("config.letterboxing.playerhud.tooltip").append("\n").append(Text.translatable("config.letterboxing.warning.tooltip"))))
                .build();

        toasts = ButtonWidget.builder(Text.translatable("config.letterboxing.toasts.buttontext").append(": ").append(getButtonBooleanState(getInstance().getAdjustToasts())),
                        (widget) -> {
                            getInstance().setAdjustToasts(!getInstance().getAdjustToasts());
                            // Refresh the screen
                            this.refreshWidgetPositions();
                            this.refreshWidgetPositions();

                        })
                .dimensions(x / 2 - 150, y / 2 + 60, 145, 20)
                .tooltip(Tooltip.of(Text.translatable("config.letterboxing.toasts.tooltip").append("\n").append(Text.translatable("config.letterboxing.warning.tooltip"))))
                .build();

        subtitles = ButtonWidget.builder(Text.translatable("config.letterboxing.subtitles.buttontext").append(": ").append(getButtonBooleanState(getInstance().getAdjustSubtitles())), // No conversion here
                        (widget) -> {
                            getInstance().setAdjustSubtitles(!getInstance().getAdjustSubtitles());
                            // Refresh the screen
                            this.refreshWidgetPositions();
                            this.refreshWidgetPositions();

                        })
                .dimensions(x / 2 + 5, y / 2 + 60, 145, 20)
                .tooltip(Tooltip.of(Text.translatable("config.letterboxing.subtitles.tooltip").append("\n").append(Text.translatable("config.letterboxing.warning.tooltip"))))
                .build();


        if (getInstance().getCurrentRatio() != ConfigAspectRatio.CUSTOM)
        {
            customWidthSlider.active = false;
            customHeightSlider.active = false;
            customWidthSlider.setTooltip(Tooltip.of(Text.translatable("config.letterboxing.widthoff.tooltip")));
            customHeightSlider.setTooltip(Tooltip.of(Text.translatable("config.letterboxing.heightoff.tooltip")));

        } else
        {
            customWidthSlider.active = true;
            customHeightSlider.active = true;
            customWidthSlider.setTooltip(Tooltip.of(Text.translatable("config.letterboxing.widthon.tooltip")));
            customHeightSlider.setTooltip(Tooltip.of(Text.translatable("config.letterboxing.heighton.tooltip")));
        }

        done = ButtonWidget.builder(Text.translatable("config.letterboxing.done.buttontext"), (widget) -> {
            // When the button is clicked, return to parent screen
            close();
        }).dimensions(x / 2 - 100, y - 20 - 5, 200, 20).build();

        // Register the button widgets as a drawable.
        this.addDrawableChild(format);
        this.addDrawableChild(customWidthSlider);
        this.addDrawableChild(customHeightSlider);
        this.addDrawableChild(normal);
        this.addDrawableChild(spectator);
        this.addDrawableChild(f1);
        this.addDrawableChild(carvedPumpkin);
        this.addDrawableChild(bossbar);
        this.addDrawableChild(chat);
        this.addDrawableChild(debug);
        this.addDrawableChild(playerHud);
        this.addDrawableChild(toasts);
        this.addDrawableChild(subtitles);
        this.addDrawableChild(done);

    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 20, 0xFFFFFF);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        client.setScreen(parent);
    }

    // This method is called when the screen is closed
    @Override
    public void removed() {
        Letterboxing.saveConfig();
        super.removed();
    }
}