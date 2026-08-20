package com.copythenerd.letterboxing.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;

public enum ConfigAspectRatio {
    NONE(0.0F, 0.0F, Text.translatable("config.letterboxing.none.buttontext")),
    HIGHDEFINITIONVERTICAL(1.0F, 1.78F, Text.translatable("config.letterboxing.highdefinitionvertical.buttontext")),
    PHONEVERTICAL(1.0F, 2.165F, Text.translatable("config.letterboxing.phonevertical.buttontext")),
    SNYDER(1.33F, 1.0F, Text.translatable("config.letterboxing.snyder.buttontext")),
    NOLAN(1.43F, 1.0F, Text.translatable("config.letterboxing.nolan.buttontext")),
    EUROPEAN(1.66F, 1.0F, Text.translatable("config.letterboxing.european.buttontext")),
    HIGHDEFINITIONHORIZONTAL(1.78F, 1.0F, Text.translatable("config.letterboxing.highdefinitionhorizontal.buttontext")),
    US(1.85F, 1.0F, Text.translatable("config.letterboxing.us.buttontext")),
    PHONEHORIZONTAL(2.165F, 1.0F, Text.translatable("config.letterboxing.phonehorizontal.buttontext")),
    CINEMASCOPE(2.35F, 1.0F, Text.translatable("config.letterboxing.cinemascope.buttontext")),
    SCOPE(2.39F, 1.0F, Text.translatable("config.letterboxing.scope.buttontext")),
    CUSTOM(0.0F, 0.0F, Text.translatable("config.letterboxing.custom.buttontext"));


    private float width;
    private float height;
    private Text text;

    ConfigAspectRatio(float width, float height, Text text) {
        this.width = width;
        this.height = height;
        this.text = text;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Text getText() {
        return text;
    }

    // Note that ConfigAspectRatio is an Enum, not a class
    // The field name stores the name of the enum
    public static final Codec<ConfigAspectRatio> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(ConfigAspectRatio::name),
            TextCodecs.CODEC.fieldOf("text").forGetter(ConfigAspectRatio::getText),
            Codec.FLOAT.fieldOf("width").forGetter(ConfigAspectRatio::getWidth),
            Codec.FLOAT.fieldOf("height").forGetter(ConfigAspectRatio::getHeight)).apply(instance, (name, text, width, height) -> {
        ConfigAspectRatio ratio = ConfigAspectRatio.valueOf(name);
        ratio.setText(text);
        ratio.setWidth(width);
        ratio.setHeight(height);
        return ratio;
    }));


    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setText(Text text) {
        this.text = text;
    }
}
