package dev.inkwell.vivid.widgets;

import dev.inkwell.vivid.screen.ConfigScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class LabelComponent extends WidgetComponent{
    protected final Text label;
    private final boolean shouldRenderHighlight;
    private int color = 0xFFFFFFFF;

    public LabelComponent(ConfigScreen parent, int x, int y, int width, int height, Text label, boolean shouldRenderHighlight) {
        super(parent, x, y, width, height);
        this.label = label;
        this.shouldRenderHighlight = shouldRenderHighlight;
    }

    @Override
    public void renderBackground(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {

    }

    @Override
    public void renderContents(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        draw(
                matrixStack,
                textRenderer,
                this.label,
                this.x + 3,
                this.textYPos(),
                0xFFFFFFFF,
                this.parent.getScale()
        );
    }

    @Override
    public void renderHighlight(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        if (this.shouldRenderHighlight) {
            super.renderHighlight(matrixStack, mouseX, mouseY, delta);
        }
    }

    public LabelComponent withColor(Style sectionColor) {
        this.color = sectionColor.getColor().getRgb();
        return this;
    }
}
