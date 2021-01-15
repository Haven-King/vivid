package dev.monarkhes.vivid.widgets.value;

import dev.monarkhes.vivid.screen.ConfigScreen;
import dev.monarkhes.vivid.util.Translatable;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class EnumDropdownWidget<T extends Enum<T>> extends DropdownWidgetComponent<T> {
    public EnumDropdownWidget(ConfigScreen parent, int x, int y, int width, int height, Supplier<@NotNull T> defaultValueSupplier, Consumer<T> changedListener, Consumer<T> saveConsumer, @NotNull T value) {
        super(parent, x, y, width, height, defaultValueSupplier, changedListener, saveConsumer, value, value.getDeclaringClass().getEnumConstants());
    }

    @Override
    protected MutableText fromValue(T value) {
        if (value instanceof Translatable) {
            return ((Translatable) value).getText();
        } else {
            return new LiteralText(value.name());
        }
    }

    @Override
    public Text getDefaultValueAsText() {
        return this.fromValue(this.getDefaultValue());
    }
}
