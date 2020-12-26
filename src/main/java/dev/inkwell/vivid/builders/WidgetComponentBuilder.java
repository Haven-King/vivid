package dev.inkwell.vivid.builders;

import dev.inkwell.vivid.screen.ConfigScreen;
import dev.inkwell.vivid.widgets.value.ValueWidgetComponent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface WidgetComponentBuilder<T> {
    ValueWidgetComponent<T> build(ConfigScreen parent, int x, int y, int width, int height, Supplier<@NotNull T> defaultValueSupplier, Consumer<T> changedListener, Consumer<T> saveConsumer, @NotNull T value);
}
