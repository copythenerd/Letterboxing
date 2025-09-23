package com.copythenerd.letterboxing.config;

import com.copythenerd.letterboxing.Letterboxing;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

// Using the singleton design pattern
public class LetterboxingConfig {

    // Current bar sizes
    private int currentHorizontalHeight = 0;
    private int currentVerticalWidth = 0;
    private ConfigAspectRatio currentRatio = ConfigAspectRatio.NONE;

    private int customHorizontalHeight = 1;
    private int customVerticalWidth = 1;

    private boolean normalVisibility = true;
    private boolean spectatorVisibility = true;
    private boolean f1Visibility = true;
    private boolean carvedPumpkinVisibility = false;

    private boolean adjustBossbar = true;
    private boolean adjustChat = true;
    private boolean adjustDebug = true;
    private boolean adjustPlayerHud = true;
    private boolean adjustToasts = true;
    private boolean adjustSubtitles = true;


    private List<Integer> values = new ArrayList<>(Arrays.asList(
            currentHorizontalHeight,
            currentVerticalWidth,
            customHorizontalHeight,
            customVerticalWidth)
    );

    private List<Boolean> states = new ArrayList<>(Arrays.asList(
            normalVisibility,
            spectatorVisibility,
            f1Visibility,
            carvedPumpkinVisibility,
            adjustBossbar,
            adjustChat,
            adjustDebug,
            adjustPlayerHud,
            adjustToasts,
            adjustSubtitles)
    );

    private static LetterboxingConfig INSTANCE;

    private final ConfigAspectRatio[] aspectRatios = ConfigAspectRatio.values();

    private LetterboxingConfig() {
    }

    private LetterboxingConfig(List<Integer> values, List<Boolean> states, ConfigAspectRatio currentRatio) {
        // Assume the values list is in the order:
        // [currentHorizontalHeight, currentVerticalWidth, customHorizontalHeight, customVerticalWidth]
        this.currentHorizontalHeight = values.get(0);
        this.currentVerticalWidth = values.get(1);
        this.customHorizontalHeight = values.get(2);
        this.customVerticalWidth = values.get(3);

        // And similarly for the states list:
        this.normalVisibility = states.get(0);
        this.spectatorVisibility = states.get(1);
        this.f1Visibility = states.get(2);
        this.carvedPumpkinVisibility = states.get(3);
        this.adjustBossbar = states.get(4);
        this.adjustChat = states.get(5);
        this.adjustDebug = states.get(6);
        this.adjustPlayerHud = states.get(7);
        this.adjustToasts = states.get(8);
        this.adjustSubtitles = states.get(9);
        this.values = values;
        this.states = states;
        this.currentRatio = currentRatio;

        Letterboxing.LOGGER.info("Loaded config from file successfully.");
    }


    // Getters and Setters for attributes
    public int getCurrentHorizontalHeight() {
        return currentHorizontalHeight;
    }

    public void setCurrentHorizontalHeight(int value) {
        this.currentHorizontalHeight = value;
    }

    public int getCurrentVerticalWidth() {
        return currentVerticalWidth;
    }

    public void setCurrentVerticalWidth(int value) {
        this.currentVerticalWidth = value;
    }

    public ConfigAspectRatio getCurrentRatio() {
        return currentRatio;
    }

    public void setCurrentRatio(ConfigAspectRatio ratio) {
        this.currentRatio = ratio;
    }

    public int getCustomHorizontalHeight() {
        return customHorizontalHeight;
    }

    public void setCustomHorizontalHeight(int value) {
        this.customHorizontalHeight = value;
    }

    public int getCustomVerticalWidth() {
        return customVerticalWidth;
    }

    public void setCustomVerticalWidth(int value) {
        this.customVerticalWidth = value;
    }

    public boolean getNormalVisibility() {
        return normalVisibility;
    }

    public void setNormalVisibility(boolean value) {
        this.normalVisibility = value;
    }

    public boolean getSpectatorVisibility() {
        return spectatorVisibility;
    }

    public void setSpectatorVisibility(boolean value) {
        this.spectatorVisibility = value;
    }

    public boolean getF1Visibility() {
        return f1Visibility;
    }

    public void setF1Visibility(boolean value) {
        this.f1Visibility = value;
    }

    public boolean getCarvedPumpkinVisibility() {
        return carvedPumpkinVisibility;
    }

    public void setCarvedPumpkinVisibility(boolean value) {
        this.carvedPumpkinVisibility = value;
    }

    public boolean getAdjustBossbar() {
        return adjustBossbar;
    }

    public void setAdjustBossbar(boolean value) {
        this.adjustBossbar = value;
    }

    public boolean getAdjustChat() {
        return adjustChat;
    }

    public void setAdjustChat(boolean value) {
        this.adjustChat = value;
    }

    public boolean getAdjustDebug() {
        return adjustDebug;
    }

    public void setAdjustDebug(boolean value) {
        this.adjustDebug = value;
    }

    public boolean getAdjustPlayerHud() {
        return adjustPlayerHud;
    }

    public void setAdjustPlayerHud(boolean value) {
        this.adjustPlayerHud = value;
    }

    public boolean getAdjustToasts() {
        return adjustToasts;
    }

    public void setAdjustToasts(boolean value) {
        this.adjustToasts = value;
    }

    public boolean getAdjustSubtitles() {
        return adjustSubtitles;
    }

    public void setAdjustSubtitles(boolean value) {
        this.adjustSubtitles = value;
    }


    // Getter for lists
    public List<Integer> getValues() {
        return Arrays.asList(
                currentHorizontalHeight,
                currentVerticalWidth,
                customHorizontalHeight,
                customVerticalWidth);
    }

    public List<Boolean> getStates() {
        return Arrays.asList(
                normalVisibility,
                spectatorVisibility,
                f1Visibility,
                carvedPumpkinVisibility,
                adjustBossbar,
                adjustChat,
                adjustDebug,
                adjustPlayerHud,
                adjustToasts,
                adjustSubtitles);
    }

    public ConfigAspectRatio getRatio() {
        return currentRatio;
    }

    // Return the current index of the enum
    public int getCurrentIndex() {
        return currentRatio.ordinal();
    }

    // Helper Function for aspect ratio index
    public void nextIndex() {
        int currentIndex = currentRatio.ordinal();
        int nextIndex = (currentIndex + 1) % aspectRatios.length;
        currentRatio = aspectRatios[nextIndex];
        //aspectRatioIndex = (aspectRatioIndex + 1) % aspectRatios.length;
    }

    // Static Factory to return the only instance
    public static LetterboxingConfig getInstance() {
        if (INSTANCE == null)
        {
            INSTANCE = new LetterboxingConfig();
        }
        return INSTANCE;
    }

    // Helper method to update INSTANCE from newly created instance that has the values loaded
    // from the config file
    public void updateFrom(LetterboxingConfig newConfig) {

        try
        {
            List<Integer> newValues = newConfig.values;
            List<Boolean> newStates = newConfig.states;
            INSTANCE.setCurrentRatio(newConfig.currentRatio);
            INSTANCE.currentHorizontalHeight = newValues.get(0);
            INSTANCE.currentVerticalWidth = newValues.get(1);
            INSTANCE.customHorizontalHeight = newValues.get(2);
            INSTANCE.customVerticalWidth = newValues.get(3);

            INSTANCE.normalVisibility = newStates.get(0);
            INSTANCE.spectatorVisibility = newStates.get(1);
            INSTANCE.f1Visibility = newStates.get(2);
            INSTANCE.carvedPumpkinVisibility = newStates.get(3);
            INSTANCE.adjustBossbar = newStates.get(4);
            INSTANCE.adjustChat = newStates.get(5);
            INSTANCE.adjustDebug = newStates.get(6);
            INSTANCE.adjustPlayerHud = newStates.get(7);
            INSTANCE.adjustToasts = newStates.get(8);
            INSTANCE.adjustSubtitles = newStates.get(9);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // Create a codec to serialize and deserialize LetterboxingConfig
    public static Codec<LetterboxingConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.listOf().fieldOf("values").forGetter(LetterboxingConfig::getValues),
            Codec.BOOL.listOf().fieldOf("states").forGetter(LetterboxingConfig::getStates),
            ConfigAspectRatio.CODEC.fieldOf("currentRatio").forGetter(LetterboxingConfig::getRatio)).apply(instance, LetterboxingConfig::new));

    public static void load(Path file) {
        try
        {
            if (!Files.exists(file))
            {
                Letterboxing.LOGGER.info("Config file does not exist. Create new file with default values.");
                //return INSTANCE;
            }
            // Read the file content as a String.
            String fileContent = Files.readString(file);

            // Parse the JSON String into a JsonElement.
            JsonElement jsonElement = JsonParser.parseString(fileContent);

            // Use the Codec to decode the JsonElement.
            DataResult<Pair<LetterboxingConfig, JsonElement>> result = CODEC.decode(JsonOps.INSTANCE, jsonElement);
            Optional<Pair<LetterboxingConfig, JsonElement>> optional = result.result();

            if (optional.isPresent())
            {
                // Create new config from file
                LetterboxingConfig newConfig = optional.get().getFirst();

                Letterboxing.LOGGER.info("Update INSTANCE from new config.");

                // Copy content of new instance over to INSTANCE
                INSTANCE.updateFrom(newConfig);

            } else
            {
                // Print error details if available.
                Letterboxing.LOGGER.error("Failed to decode config: " + result.error());
                //return INSTANCE;
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public boolean save(Path file) {
        // Use the Codec to encode this instance into JSON.
        DataResult<JsonElement> result = CODEC.encodeStart(JsonOps.INSTANCE, this);
        Optional<JsonElement> encoded = result.result();
        if (encoded.isPresent())
        {
            String fileContent = encoded.get().toString();
            try
            {
                // Write the JSON String to the file.
                Files.writeString(file, fileContent);
                return true;
            } catch (IOException e)
            {
                e.printStackTrace();
                return false;
            }
        } else
        {
            Letterboxing.LOGGER.error("Failed to encode config: " + result.error());
            return false;
        }
    }
}
