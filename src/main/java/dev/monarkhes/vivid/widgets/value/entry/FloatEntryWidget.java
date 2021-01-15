package dev.monarkhes.vivid.widgets.value.entry;

import dev.monarkhes.vivid.screen.ConfigScreen;
import dev.monarkhes.vivid.util.Alignment;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class FloatEntryWidget extends NumberEntryWidget<Float> {
    public FloatEntryWidget(ConfigScreen parent, int x, int y, int width, int height, Alignment alignment, Supplier<@NotNull Float> defaultValueSupplier, Consumer<Float> changedListener, Consumer<Float> saveConsumer, @NotNull Float value) {
        super(parent, x, y, width, height, alignment, defaultValueSupplier, changedListener, saveConsumer, value);
        this.setTextPredicate(string -> {
            try {
                Double.parseDouble(string);
                return true;
            } catch (NumberFormatException ignored) {
                return false;
            }
        });
    }

    @Override
    protected String valueOf(Float value) {
        return String.valueOf(value);
    }

    @Override
    protected Float emptyValue() {
        return 0F;
    }

    @Override
    protected Optional<Float> parse(String value) {
        try {
            return Optional.of(Float.valueOf(value));
        } catch (NumberFormatException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public boolean isWithinBounds(Float value) {
        return (min == null || value >= min) && (max == null || value <= max);
    }
}
