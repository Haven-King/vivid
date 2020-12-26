package dev.inkwell.vivid.widgets.value;

import dev.inkwell.vivid.screen.ConfigScreen;
import dev.inkwell.vivid.util.Translatable;
import dev.inkwell.vivid.widgets.TextButton;
import dev.inkwell.vivid.widgets.WidgetComponent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class EnumSelectorComponent<T extends Enum<T>> extends ValueWidgetComponent<T> {
    protected final WidgetComponent[] children;
    private final int buttonWidth;

    public EnumSelectorComponent(ConfigScreen parent, int x, int y, int width, int height, Supplier<@NotNull T> defaultValueSupplier, Consumer<T> changedListener, Consumer<T> saveConsumer, @NotNull T value) {
        super(parent, x, y, width, height, defaultValueSupplier, changedListener, saveConsumer, value);

        T[] possibleValues = value.getDeclaringClass().getEnumConstants();

        this.buttonWidth = width / possibleValues.length;
        this.children = new WidgetComponent[possibleValues.length];

        for (int i = 0; i < possibleValues.length; ++i) {
            this.children[i] = new EnumButton(parent, x + buttonWidth * i, y, buttonWidth, height, possibleValues[i]);
        }
    }

    @Override
    public boolean hasError() {
        return false;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta, boolean shouldRenderHighlight) {
        super.render(matrixStack, mouseX, mouseY, delta, shouldRenderHighlight);

        for (WidgetComponent child : this.children) {
            child.render(matrixStack, mouseX, mouseY, delta, shouldRenderHighlight);
        }
    }

    @Override
    public void scroll(int amount) {
        super.scroll(amount);
        for (WidgetComponent button : this.children) {
            button.scroll(amount);
        }
    }

    @Override
    public void tick() {
        super.tick();

        for (WidgetComponent child : this.children) {
            child.tick();
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean bl = false;

        for (WidgetComponent child : this.children) {
            bl |= child.mouseClicked(mouseX, mouseY, button);
        }

        return bl;
    }

    @Override
    public void renderBackground(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {

    }

    @Override
    public void renderContents(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {

    }

    @Override
    public Text getDefaultValueAsText() {
        return this.getDefaultValue() instanceof Translatable
                ? ((Translatable) this.getDefaultValue()).getText()
                : new LiteralText(this.getDefaultValue().name());
    }

    @Override
    public void setX(int x) {
        super.setX(x);

        for (int i = 0; i < this.children.length; ++i) {
            this.children[i].setX(x + i * this.buttonWidth);
        }
    }

    @Override
    public void setY(int y) {
        super.setY(y);

        for (WidgetComponent button : this.children) {
            button.setY(y);
        }
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);

        for (WidgetComponent button : this.children) {
            button.setWidth(width);
        }
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);

        for (WidgetComponent button : this.children) {
            button.setHeight(height);
        }

        this.setY(this.y);
    }

    class EnumButton extends TextButton{
        private final T value;

        public EnumButton(ConfigScreen parent, int x, int y, int width, int height, T value) {
            super(
                    parent,
                    x,
                    y,
                    width,
                    height,
                    0,
                    value instanceof Translatable
                            ? ((Translatable) value).getText()
                            : new LiteralText(value.name()),
                    null);
            this.value = value;
        }

        @Override
        public void renderBackground(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
            this.color = this.value == EnumSelectorComponent.this.getValue()
                    ? 0x80FFFFFF
                    : 0x20FFFFFF;

            super.renderBackground(matrixStack, mouseX, mouseY, delta);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (isMouseOver(mouseX, mouseY) && button == GLFW.GLFW_MOUSE_BUTTON_1) {
                EnumSelectorComponent.this.setValue(this.value);
            }

            return false;
        }
    }
}
