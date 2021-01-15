package dev.monarkhes.vivid.widgets.value.entry;

import dev.monarkhes.vivid.screen.ConfigScreen;
import dev.monarkhes.vivid.util.Alignment;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LongEntryWidget extends NumberEntryWidget<Long> {
    public LongEntryWidget(ConfigScreen parent, int x, int y, int width, int height, Alignment alignment, Supplier<@NotNull Long> defaultValueSupplier, Consumer<Long> changedListener, Consumer<Long> saveConsumer, @NotNull Long value) {
        super(parent, x, y, width, height, alignment, defaultValueSupplier, changedListener, saveConsumer, value);
        this.setTextPredicate(string -> {
            try {
                Long.parseLong(string);
                return true;
            } catch (NumberFormatException ignored) {
                return false;
            }
        });
    }

    @Override
    protected String valueOf(Long value) {
        return String.valueOf(value);
    }

    @Override
    protected Long emptyValue() {
        return 0L;
    }

    @Override
    protected Optional<Long> parse(String value) {
        try {
            return Optional.of(Long.valueOf(value));
        } catch (NumberFormatException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public boolean isWithinBounds(Long value) {
        return (min == null || value >= min) && (max == null || value <= max);
    }
}
