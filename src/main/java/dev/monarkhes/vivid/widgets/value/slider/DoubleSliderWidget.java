package dev.monarkhes.vivid.widgets.value.slider;

import dev.monarkhes.vivid.screen.ConfigScreen;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class DoubleSliderWidget extends SliderWidget<Double> {
    public DoubleSliderWidget(ConfigScreen parent, int x, int y, int width, int height, Supplier<@NotNull Double> defaultValueSupplier, Consumer<Double> changedListener, Consumer<Double> saveConsumer, @NotNull Double value, @NotNull Double min, @NotNull Double max) {
        super(parent, x, y, width, height, defaultValueSupplier, changedListener, saveConsumer, value, min, max);
    }

    @Override
    protected Double subtract(Double left, Double right) {
        return left - right;
    }

    @Override
    protected Double add(Double left, Double right) {
        return left + right;
    }

    @Override
    protected Double multiply(Double aDouble, double d) {
        return aDouble * d;
    }

    @Override
    public boolean isWithinBounds(Double value) {
        return value >= this.min && value <= this.max;
    }

    @Override
    public Text getDefaultValueAsText() {
        return new LiteralText(this.getDefaultValue().toString());
    }

    @Override
    protected float getPercentage() {
        return (float) ((this.getValue() - getMin()) / (float) (getMax() - getMin()));
    }

    @Override
    protected String stringValue() {
        return this.getValue().toString();
    }
}
