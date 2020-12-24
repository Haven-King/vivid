package dev.inkwell.vivid.widgets;

import dev.inkwell.vivid.DrawableExtensions;
import dev.inkwell.vivid.VividConfig;
import dev.inkwell.vivid.screen.ConfigScreen;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.TickableElement;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public abstract class WidgetComponent implements Element, DrawableExtensions, TickableElement {
    private final ConfigScreen parent;
    private final List<Text> tooltips = new ArrayList<>();
    protected int x, y, width, height;
    protected float hoverOpacity = 0F;

    protected float lastMouseX;
    protected float lastMouseY;
    private boolean focused;

    public WidgetComponent(ConfigScreen parent, int x, int y, int width, int height) {
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public final void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        this.lastMouseX = mouseX;
        this.lastMouseY = mouseY;

        this.renderBackground(matrixStack, mouseX, mouseY, delta);
        this.renderHighlight(matrixStack, mouseX, mouseY, delta);
        this.renderContents(matrixStack, mouseX, mouseY, delta);

        if (this.isMouseOver(mouseX, mouseY)) {
            this.parent.addTooltips(this.tooltips);
        }
    }

    protected abstract void renderBackground(MatrixStack matrixStack, int mouseX, int mouseY, float delta);

    protected void renderHighlight(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        fill(matrixStack, this.x, this.y, this.width, this.y + this.height, this.highlightColor(), this.hoverOpacity * 0.75F);
    }

    protected abstract void renderContents(MatrixStack matrixStack, int mouseX, int mouseY, float delta);

    protected int highlightColor() {
        return 0xFFFFFFFF;
    }

    public void scroll(int amount) {
        this.y += amount;
    }

    public final void addTooltips(List<Text> tooltips) {
        this.tooltips.addAll(tooltips);
    }

    public void addTooltipsToList(List<Text> tooltips) {
        tooltips.addAll(this.tooltips);
    }

    public boolean holdsFocus() {
        return false;
    }

    public void setFocus(boolean focused) {
        this.focused = this.holdsFocus() && focused;
    }

    public final boolean isFocused() {
        return this.focused;
    }

    @Override
    public void tick() {
        if (VividConfig.Animation.ENABLED) {
            if (isMouseOver(this.lastMouseX, this.lastMouseY) && (this.parent.getFocused() == null || this.parent.getFocused() == this)) {
                this.hoverOpacity = Math.min(1F, VividConfig.Animation.SPEED + this.hoverOpacity);
            } else {
                this.hoverOpacity = Math.max(0F, this.hoverOpacity - VividConfig.Animation.SPEED);
            }
        } else {
            this.hoverOpacity = this.isMouseOver(this.lastMouseX, this.lastMouseY) ? 1F : 0F;
        }
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= this.x && mouseX <= this.x + this.width
                && mouseY >= this.y && mouseY <= this.y + this.height;
    }
}
