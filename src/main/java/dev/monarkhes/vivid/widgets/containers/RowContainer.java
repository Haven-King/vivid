package dev.monarkhes.vivid.widgets.containers;

import dev.monarkhes.vivid.screen.ConfigScreen;
import dev.monarkhes.vivid.widgets.WidgetComponent;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.NotNull;

public class RowContainer extends ComponentContainer {
    public RowContainer(ConfigScreen parent, int x, int y, int index, boolean shouldRenderHighlight, @NotNull WidgetComponent child, WidgetComponent... children) {
        super(parent, x, y, index, shouldRenderHighlight, child, children);
    }

    @Override
    protected void init() {
        for (WidgetComponent thing : this.children) {
            thing.setX(this.x + this.width);
            thing.setY(this.y);

            if (thing instanceof ComponentContainer) {
                ((ComponentContainer) thing).init();
            }

            this.width = this.width + thing.getWidth();
            this.height = Math.max(this.height, thing.getHeight());
        }
    }

    @Override
    public void renderBackground(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        for (int column = 0; column < this.children.size(); ++column) {
            int color = this.index % 2 == 0
                    ? column % 2 == 0 ? 0x40FFFFFF : 0x30FFFFFF
                    : column % 2 == 0 ? 0x30FFFFFF : 0x20FFFFFF;

            WidgetComponent child = this.children.get(column);
            DrawableHelper.fill(matrixStack, child.getX(), child.getY(), child.getX() + child.getWidth(), child.getY() + child.getHeight(), color);
        }
    }
}
