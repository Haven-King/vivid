package dev.monarkhes.vivid.widgets.value.entry;

import dev.monarkhes.vivid.constraints.Bounded;
import dev.monarkhes.vivid.screen.ConfigScreen;
import dev.monarkhes.vivid.util.Alignment;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class IntegerEntryWidget extends NumberEntryWidget<Integer> implements Bounded<Integer> {
    public IntegerEntryWidget(ConfigScreen parent, int x, int y, int width, int height, Alignment alignment, Supplier<@NotNull Integer> defaultValueSupplier, Consumer<Integer> changedListener, Consumer<Integer> saveConsumer, @NotNull Integer value) {
        super(parent, x, y, width, height, alignment, defaultValueSupplier, changedListener, saveConsumer, value);
        this.setTextPredicate(string -> {
            try {
                long l = Long.parseLong(string);
                return l <= Integer.MAX_VALUE && l >= Integer.MIN_VALUE;
            } catch (NumberFormatException ignored) {
                return false;
            }
        });
    }

    @Override
    protected String valueOf(Integer value) {
        return String.valueOf(value);
    }

    @Override
    protected Integer emptyValue() {
        return 0;
    }

    @Override
    protected Optional<Integer> parse(String value) {
        try {
            return Optional.of(Integer.valueOf(value));
        } catch (NumberFormatException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public boolean isWithinBounds(Integer value) {
        return (min == null || value >= min) && (max == null || value <= max);
    }
}
