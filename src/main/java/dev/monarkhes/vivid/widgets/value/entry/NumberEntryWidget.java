package dev.monarkhes.vivid.widgets.value.entry;

import dev.monarkhes.vivid.constraints.Bounded;
import dev.monarkhes.vivid.screen.ConfigScreen;
import dev.monarkhes.vivid.util.Alignment;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class NumberEntryWidget<T extends Number> extends TextWidgetComponent<T> implements Bounded<T> {
    protected T min = null;
    protected T max = null;

    public NumberEntryWidget(ConfigScreen parent, int x, int y, int width, int height, Alignment alignment, Supplier<@NotNull T> defaultValueSupplier, Consumer<T> changedListener, Consumer<T> saveConsumer, @NotNull T value) {
        super(parent, x, y, width, height, alignment, defaultValueSupplier, changedListener, saveConsumer, value);
    }

    @Override
    public boolean passes() {
        return super.passes() && this.isWithinBounds(this.getValue());
    }

    @Override
    public void addConstraintTooltips(List<Text> tooltips) {
        Bounded.super.addConstraintTooltips(tooltips);
    }

    @Override
    public void setMin(@Nullable T min) {
        this.min = min;
    }

    @Override
    public void setMax(@Nullable T max) {
        this.max = max;
    }

    @Override
    public @Nullable T getMin() {
        return this.min;
    }

    @Override
    public @Nullable T getMax() {
        return this.max;
    }

    @Override
    public void setBounds(T min, T max) {
        this.setMin(min);
        this.setMax(max);
    }
}
