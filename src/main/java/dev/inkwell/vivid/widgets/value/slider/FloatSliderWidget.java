package dev.inkwell.vivid.widgets.value.slider;

import dev.inkwell.vivid.screen.ConfigScreen;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FloatSliderWidget extends SliderWidget<Float> {
    public FloatSliderWidget(ConfigScreen parent, int x, int y, int width, int height, Supplier<@NotNull Float> defaultValueSupplier, Consumer<Float> changedListener, Consumer<Float> saveConsumer, @NotNull Float value, @NotNull Float min, @NotNull Float max) {
        super(parent, x, y, width, height, defaultValueSupplier, changedListener, saveConsumer, value, min, max);
    }

    @Override
    protected Float subtract(Float left, Float right) {
        return left - right;
    }

    @Override
    protected Float add(Float left, Float right) {
        return left + right;
    }

    @Override
    protected Float multiply(Float aFloat, double d) {
        return (float) (aFloat * d);
    }

    @Override
    public boolean isWithinBounds(Float value) {
        return value >= this.min && value <= this.max;
    }

    @Override
    public Text getDefaultValueAsText() {
        return new LiteralText(this.getDefaultValue().toString());
    }

    @Override
    protected float getPercentage() {
        return ((this.getValue() - getMin()) / (float) (getMax() - getMin()));
    }

    @Override
    protected String stringValue() {
        return this.getValue().toString();
    }
}
