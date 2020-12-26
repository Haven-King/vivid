package dev.inkwell.vivid.widgets;

import dev.inkwell.vivid.DrawableExtensions;
import dev.inkwell.vivid.VividConfig;
import dev.inkwell.vivid.screen.ConfigScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.TickableElement;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public abstract class WidgetComponent implements Element, DrawableExtensions, TickableElement {
    protected final ConfigScreen parent;
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

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta, boolean shouldRenderHighlight) {
        this.lastMouseX = mouseX;
        this.lastMouseY = mouseY;

        this.renderBackground(matrixStack, mouseX, mouseY, delta);

        if (shouldRenderHighlight) {
            this.renderHighlight(matrixStack, mouseX, mouseY, delta);
        }

        this.renderContents(matrixStack, mouseX, mouseY, delta);

        if (this.isMouseOver(mouseX, mouseY)) {
            this.addTooltipsToList(parent.tooltips);
        }
    }

    public abstract void renderBackground(MatrixStack matrixStack, int mouseX, int mouseY, float delta);

    public void renderHighlight(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        fill(matrixStack, this.x, this.y, this.x + this.width, this.y + this.height, this.highlightColor(), this.hoverOpacity * 0.75F);
    }

    public abstract void renderContents(MatrixStack matrixStack, int mouseX, int mouseY, float delta);

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

    public void setFocused(boolean focused) {
        this.focused = this.holdsFocus() && focused;
    }

    public final boolean isFocused() {
        return this.focused;
    }

    @Override
    public void tick() {
        if (VividConfig.Animation.ENABLED) {
            if (isMouseOver(this.lastMouseX, this.lastMouseY)) {
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    protected float textYPos() {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        return this.y + this.height / 2F - (textRenderer.fontHeight * parent.getScale() + 4 * parent.getScale()) / 2F;
    }
}
