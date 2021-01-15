package dev.monarkhes.vivid.builders;

import dev.monarkhes.vivid.screen.ConfigScreen;
import dev.monarkhes.vivid.widgets.WidgetComponent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface WidgetComponentBuilder<T> {
    WidgetComponent build(ConfigScreen parent, int x, int y, int width, int height, Supplier<@NotNull T> defaultValueSupplier, Consumer<T> changedListener, Consumer<T> saveConsumer, @NotNull T value);
}
