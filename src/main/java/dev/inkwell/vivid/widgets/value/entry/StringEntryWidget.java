package dev.inkwell.vivid.widgets.value.entry;

import dev.inkwell.vivid.screen.ConfigScreen;
import dev.inkwell.vivid.util.Alignment;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class StringEntryWidget extends TextWidgetComponent<String> {
    public StringEntryWidget(ConfigScreen parent, int x, int y, int width, int height, Alignment alignment, Supplier<@NotNull String> defaultValueSupplier, Consumer<String> changedListener, Consumer<String> saveConsumer, @NotNull String value) {
        super(parent, x, y, width, height, alignment, defaultValueSupplier, changedListener, saveConsumer, value);
    }

    @Override
    protected String valueOf(String value) {
        return value;
    }

    @Override
    protected String emptyValue() {
        return "";
    }

    @Override
    protected Optional<String> parse(String value) {
        return Optional.of(value);
    }
}
