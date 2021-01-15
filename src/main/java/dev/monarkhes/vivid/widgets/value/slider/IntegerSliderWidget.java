package dev.monarkhes.vivid.widgets.value.slider;

import dev.monarkhes.vivid.screen.ConfigScreen;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class IntegerSliderWidget extends SliderWidget<Integer> {
    public IntegerSliderWidget(ConfigScreen parent, int x, int y, int width, int height, Supplier<@NotNull Integer> defaultValueSupplier, Consumer<Integer> changedListener, Consumer<Integer> saveConsumer, @NotNull Integer value, @NotNull Integer min, @NotNull Integer max) {
        super(parent, x, y, width, height, defaultValueSupplier, changedListener, saveConsumer, value, min, max);
    }

    @Override
    protected Integer subtract(Integer left, Integer right) {
        return left - right;
    }

    @Override
    protected Integer add(Integer left, Integer right) {
        return left + right;
    }

    @Override
    protected Integer multiply(Integer integer, double d) {
        return (int) (integer * d);
    }

    @Override
    public boolean isWithinBounds(Integer value) {
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
