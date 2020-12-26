package dev.inkwell.vivid.widgets.value.entry;

import dev.inkwell.vivid.screen.ConfigScreen;
import dev.inkwell.vivid.util.Alignment;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DoubleEntryWidget extends NumberEntryWidget<Double> {
    public DoubleEntryWidget(ConfigScreen parent, int x, int y, int width, int height, Alignment alignment, Supplier<@NotNull Double> defaultValueSupplier, Consumer<Double> changedListener, Consumer<Double> saveConsumer, @NotNull Double value) {
        super(parent, x, y, width, height, alignment, defaultValueSupplier, changedListener, saveConsumer, value);
    }

    @Override
    public boolean isWithinBounds(Double value) {
        return (min == null || value >= min) && (max == null || value <= max);
    }

    @Override
    protected String valueOf(Double value) {
        return String.valueOf(value);
    }

    @Override
    protected Double emptyValue() {
        return 0D;
    }

    @Override
    protected Optional<Double> parse(String value) {
        try {
            return Optional.of(Double.valueOf(value));
        } catch (NumberFormatException ignored) {
            return Optional.empty();
        }
    }
}
