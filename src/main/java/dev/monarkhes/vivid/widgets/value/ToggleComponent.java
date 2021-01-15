package dev.monarkhes.vivid.widgets.value;

import dev.monarkhes.vivid.screen.ConfigScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ToggleComponent extends ValueWidgetComponent<Boolean> {
    public ToggleComponent(ConfigScreen parent, int x, int y, int width, int height, Supplier<@NotNull Boolean> defaultValueSupplier, Consumer<Boolean> changedListener, Consumer<Boolean> saveConsumer, @NotNull Boolean value) {
        super(parent, x, y, width, height, defaultValueSupplier, changedListener, saveConsumer, value);
    }

    @Override
    public Text getDefaultValueAsText() {
        return new LiteralText(this.getDefaultValue().toString());
    }

    @Override
    public void renderBackground(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isMouseOver(mouseX, mouseY) && button == 0) {
            this.setValue(!this.getValue());
            return true;
        }

        return false;
    }

    @Override
    public void renderContents(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        float x1 = this.x + this.width * 0.5F - 5;
        float y1 = this.y + this.height / 2F - 5;
        float x2 = this.x + this.width * 0.5F + 5;
        float y2 = this.y + this.height / 2F + 5;

        fill(matrixStack    , x1, y1, x2, y2, 0xFFFFFFFF, 0.4F);

        if (this.getValue()) {
            float padding = 2;
            fill(matrixStack, x1 + padding, y1 + padding, x2 - padding, y2 - padding, 0xFFFFFFFF, 0.8F);
        }
    }

    @Override
    public boolean hasError() {
        return false;
    }
}
