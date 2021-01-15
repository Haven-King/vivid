package dev.monarkhes.vivid.widgets.value;

import dev.monarkhes.vivid.screen.ConfigScreen;
import dev.monarkhes.vivid.widgets.TextButton;
import dev.monarkhes.vivid.widgets.WidgetComponent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class DropdownWidgetComponent<T> extends ShadedWidgetComponent<T> {
    private final T[] possibleValues;
    private final TextButton button;
    private final TextButton[] buttons;

    public DropdownWidgetComponent(ConfigScreen parent, int x, int y, int width, int height, Supplier<@NotNull T> defaultValueSupplier, Consumer<T> changedListener, Consumer<T> saveConsumer, @NotNull T value, T[] possibleValues) {
        super(parent, x, y, width, height, defaultValueSupplier, changedListener, saveConsumer, value);
        this.possibleValues = possibleValues;

        this.button = new TextButton(parent, x, y, width, height, 0, this.fromValue(value), button -> {
            this.isShadeDrawn = true;
            return true;
        });

        this.buttons = new TextButton[possibleValues.length];

        for (int i = 0; i < this.buttons.length; ++i) {
            int j = i;
            buttons[i] = new TextButton(parent, x, y + height * i, width, height, i % 2 == 0 ? 0x30FFFFFF : 0x20FFFFFF, this.fromValue(possibleValues[i]), button -> {
                this.setValue(possibleValues[j]);
                this.button.setText(this.fromValue(possibleValues[j]));
                this.isShadeDrawn = false;
                this.setFocused(false);
                parent.setFocused(null);
                return true;
            });
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta, boolean shouldRenderHighlight) {
        super.render(matrixStack, mouseX, mouseY, delta, shouldRenderHighlight);

        if (this.isShadeDrawn) {
            for (TextButton button : this.buttons) {
                button.render(matrixStack, mouseX, mouseY, delta, shouldRenderHighlight);
            }
        } else {
            this.button.render(matrixStack, mouseX, mouseY, delta, shouldRenderHighlight);
        }
    }

    @Override
    public void renderContents(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean bl;

        if (this.isShadeDrawn) {
            bl = false;

            for (TextButton btn : this.buttons) {
                bl |= btn.mouseClicked(mouseX, mouseY, button);
            }

            if (!bl) {
                this.setFocused(false);
                this.parent.setFocused(null);
            }

        } else {
            bl = this.button.mouseClicked(mouseX, mouseY, button);

            if (bl) {
                this.setFocused(true);
                this.parent.setFocused(this);
            }

        }

        return bl;
    }

    @Override
    public void scroll(int amount) {
        super.scroll(amount);

        this.button.scroll(amount);

        for (WidgetComponent button : this.buttons) {
            button.scroll(amount);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isShadeDrawn) {
            for (TextButton button : this.buttons) {
                button.tick();
            }
        } else {
            this.button.tick();
        }
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);
        this.isShadeDrawn = this.isFocused();
    }

    @Override
    public boolean hasError() {
        return false;
    }

    protected abstract MutableText fromValue(T value);

    @Override
    public void setX(int x) {
        super.setX(x);
        this.button.setX(x);
        for (TextButton button : this.buttons) {
            button.setX(x);
        }
    }

    @Override
    public void setY(int y) {
        super.setY(y);
        this.button.setY(y);

        for (int i = 0; i < this.buttons.length; ++i) {
            this.buttons[i].setY(y + i * this.height);
        }
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        this.button.setWidth(width);

        for (TextButton button : this.buttons) {
            button.setWidth(width);
        }
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        this.button.setHeight(height);

        for (TextButton button : this.buttons) {
            button.setHeight(height);
        }

        this.setY(this.y);
    }
}
