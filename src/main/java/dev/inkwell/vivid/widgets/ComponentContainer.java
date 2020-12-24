package dev.inkwell.vivid.widgets;

import dev.inkwell.vivid.screen.ConfigScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ComponentContainer extends WidgetComponent{
    protected final List<WidgetComponent> children = new ArrayList<>();

    public ComponentContainer(ConfigScreen parent, @NotNull WidgetComponent child, WidgetComponent... children) {
        super(parent, 0, 0, 0, 0);
        this.init(child, children);
    }

    protected void init(WidgetComponent child, WidgetComponent... children) {
        this.x = child.x;
        this.y = child.y;
        this.width = child.width;
        this.height = child.height;

        for (WidgetComponent thing : children) {
            this.children.add(thing);

            int x1 = Math.min(this.x, thing.x);
            int y1 = Math.min(this.y, thing.y);
            int x2 = Math.max(this.x + this.width, thing.x + thing.width);
            int y2 = Math.max(this.y + this.height, thing.y + thing.height);

            this.x = x1;
            this.y = y1;
            this.width = x2 - x1;
            this.height = y2 - y1;
        }
    }

    @Override
    protected void renderBackground(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        this.children.forEach(child -> child.renderBackground(matrixStack, mouseX, mouseY, delta));
    }

    @Override
    protected void renderHighlight(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        this.children.forEach(child -> child.renderHighlight(matrixStack, mouseX, mouseY, delta));
    }

    @Override
    protected void renderContents(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        this.children.forEach(child -> child.renderContents(matrixStack, mouseX, mouseY, delta));
    }

    @Override
    public void tick() {
        this.children.forEach(WidgetComponent::tick);
    }

    @Override
    public void scroll(int amount) {
        super.scroll(amount);
        this.children.forEach(child -> child.scroll(amount));
    }

    @Override
    public boolean holdsFocus() {
        for (WidgetComponent child : this.children) {
            if (child.holdsFocus()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        for (WidgetComponent child : this.children) {
            if (child.isMouseOver(mouseX, mouseY)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        this.children.forEach(child -> child.mouseMoved(mouseX, mouseY));
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
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        boolean bl = false;

        for (WidgetComponent child : this.children) {
            bl |= child.mouseReleased(mouseX, mouseY, button);
        }

        return bl;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        boolean bl = false;

        for (WidgetComponent child : this.children) {
            bl |= child.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }

        return bl;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        boolean bl = false;

        for (WidgetComponent child : this.children) {
            bl |= child.mouseScrolled(mouseX, mouseY, amount);
        }

        return bl;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean bl = false;

        for (WidgetComponent child : this.children) {
            bl |= child.keyPressed(keyCode, scanCode, modifiers);
        }

        return bl;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        boolean bl = false;

        for (WidgetComponent child : this.children) {
            bl |= child.keyReleased(keyCode, scanCode, modifiers);
        }

        return bl;
    }

    @Override
    public boolean charTyped(char chr, int keyCode) {
        boolean bl = false;

        for (WidgetComponent child : this.children) {
            bl |= child.charTyped(chr, keyCode);
        }

        return bl;
    }

    @Override
    public boolean changeFocus(boolean lookForwards) {
        boolean bl = false;

        for (WidgetComponent child : this.children) {
            bl |= child.changeFocus(lookForwards);
        }

        return bl;
    }
}
