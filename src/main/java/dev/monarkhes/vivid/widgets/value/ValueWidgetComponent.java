package dev.monarkhes.vivid.widgets.value;

import dev.monarkhes.vivid.constraints.Constraint;
import dev.monarkhes.vivid.screen.ConfigScreen;
import dev.monarkhes.vivid.widgets.Mutable;
import dev.monarkhes.vivid.widgets.WidgetComponent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class ValueWidgetComponent<T> extends WidgetComponent implements Mutable {
    private final Supplier<T> defaultValueSupplier;
    private final T defaultValue;
    private final Consumer<T> changedListener;
    private final Consumer<T> saveConsumer;
    private final T initialValue;
    private final Text defaultValueText;

    private T value;

    public ValueWidgetComponent(ConfigScreen parent, int x, int y, int width, int height, Supplier<@NotNull T> defaultValueSupplier, Consumer<T> changedListener, Consumer<T> saveConsumer, @NotNull T value) {
        super(parent, x, y, width, height);
        this.defaultValueSupplier = defaultValueSupplier;
        this.defaultValue = defaultValueSupplier.get();
        this.changedListener = changedListener;
        this.saveConsumer = saveConsumer;
        this.initialValue = value;
        this.value = value;
        this.defaultValueText = this.getDefaultValueAsText();
    }

    @Override
    public final void save() {
        if (!this.hasError()) {
            this.saveConsumer.accept(value);
        }
    }

    @Override
    public final void reset() {
        this.value = this.defaultValueSupplier.get();
    }

    @Override
    public boolean hasChanged() {
        return !this.initialValue.equals(this.value);
    }

    protected final void setValue(T value) {
        if (this.value == null || !this.value.equals(value)) {
            T oldValue = this.value;
            this.value = value;

            if (this.hasError()) {
                this.value = oldValue;
            } else {
                this.changedListener.accept(value);
            }
        }
    }

    public final T getValue() {
        return this.value;
    }

    public final T getDefaultValue() {
        return this.defaultValue;
    }

    protected abstract Text getDefaultValueAsText();

    public final Text getDefaultValueText() {
        return this.defaultValueText;
    }

    @Override
    public void addTooltipsToList(List<Text> tooltips) {
        super.addTooltipsToList(tooltips);

        if (this instanceof Constraint) {
            ((Constraint) this).addConstraintTooltips(tooltips);
        }

        tooltips.add(new TranslatableText("vivid.default", this.getDefaultValueAsText()));
    }
}
