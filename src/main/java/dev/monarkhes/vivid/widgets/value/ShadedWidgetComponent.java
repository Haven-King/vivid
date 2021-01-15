package dev.monarkhes.vivid.widgets.value;

import dev.monarkhes.vivid.screen.ConfigScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static dev.monarkhes.vivid.Vivid.BLUR;

public abstract class ShadedWidgetComponent<T> extends ValueWidgetComponent<T> {
    protected boolean isShadeDrawn = false;

    public ShadedWidgetComponent(ConfigScreen parent, int x, int y, int width, int height, Supplier<@NotNull T> defaultValueSupplier, Consumer<T> changedListener, Consumer<T> saveConsumer, @NotNull T value) {
        super(parent, x, y, width, height, defaultValueSupplier, changedListener, saveConsumer, value);
    }

    @Override
    public void renderBackground(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        if (this.isShadeDrawn) {
            fill(matrixStack, 0, 0, 10000, 10000, 0, 0.5F);
            BLUR.setUniformValue("Start", 0F, 0F);
            BLUR.setUniformValue("End", 1F, 1F);
            BLUR.setUniformValue("Progress", 1F);
            BLUR.setUniformValue("Radius", 5F);
            BLUR.render(delta);
        }
    }

    @Override
    public boolean holdsFocus() {
        return true;
    }
}
