package dev.inkwell.vivid.widgets;

import dev.inkwell.vivid.screen.ConfigScreen;
import net.minecraft.client.util.math.MatrixStack;

public class SpacerComponent extends WidgetComponent{
    public SpacerComponent(ConfigScreen parent, int x, int y, int width, int height) {
        super(parent, x, y, width, height);
    }

    @Override
    public void renderBackground(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {

    }

    @Override
    public void renderContents(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {

    }

    @Override
    public void renderHighlight(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {

    }
}
