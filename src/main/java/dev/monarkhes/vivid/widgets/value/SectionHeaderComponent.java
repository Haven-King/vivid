package dev.monarkhes.vivid.widgets.value;

import dev.monarkhes.vivid.screen.ConfigScreen;
import dev.monarkhes.vivid.widgets.LabelComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class SectionHeaderComponent extends LabelComponent {
    public SectionHeaderComponent(ConfigScreen parent, int x, int y, int width, int height, Text label, boolean shouldRenderHighlight) {
        super(parent, x, y, width, height, label, shouldRenderHighlight);
    }

    @Override
    public void renderContents(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        draw(
                matrixStack,
                textRenderer,
                this.label,
                this.x + 3,
                (int) (this.y + this.height * 0.9 - (textRenderer.fontHeight * this.parent.getScale() * 1.5)),
                0xFFFFFFFF,
                this.parent.getScale()
        );
    }
}
