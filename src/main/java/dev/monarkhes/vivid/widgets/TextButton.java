package dev.monarkhes.vivid.widgets;

import dev.monarkhes.vivid.screen.ConfigScreen;
import dev.monarkhes.vivid.util.Alignment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import org.lwjgl.glfw.GLFW;

public class TextButton extends WidgetComponent {
    private final Action onClick;
    private final Alignment alignment;

    protected int color;
    private MutableText text;

    public TextButton(ConfigScreen parent, int x, int y, int width, int height, int color, MutableText text, Action onClick) {
        this(parent, x, y, width, height, color, text, Alignment.CENTER, onClick);
    }

    public TextButton(ConfigScreen parent, int x, int y, int width, int height, int color, MutableText text, Alignment alignment, Action onClick) {
        super(parent, x, y, width, height);
        this.color = color;
        this.alignment = alignment;
        this.text = text;
        this.onClick = onClick;
    }

    @Override
    public void renderBackground(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        DrawableHelper. fill(matrixStack, this.x, this.y, this.x + this.width, this.y + this.height, this.color);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void renderContents(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        float x;

        float width = textRenderer.getWidth(this.text);

        switch (this.alignment) {
            case LEFT:
                x = this.x + 3;
                break;
            case CENTER:
                x = this.x + this.width / 2F - (width / 2F * this.parent.getScale());
                break;
            case RIGHT:
                x = this.x + this.width - 3 - width * this.parent.getScale();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + this.alignment);
        }

        draw(
                matrixStack,
                textRenderer,
                this.text,
                x,
                this.textYPos(),
                0xFFFFFFFF,
                this.parent.getScale()
        );
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isMouseOver(mouseX, mouseY) && button == GLFW.GLFW_MOUSE_BUTTON_1) {
            return this.onClick.onClick(this);
        }

        return false;
    }

    public void setText(MutableText text) {
        this.text = text;
    }

    @FunctionalInterface
    public interface Action {
        boolean onClick(TextButton button);
    }
}
