package dev.inkwell.vivid.screen;

import dev.inkwell.vivid.DrawableExtensions;
import dev.inkwell.vivid.VividConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class FancyButton extends ButtonWidget implements DrawableExtensions {
    private ConfigScreen parent;
    private float hoverOpacity = 0F;

    public FancyButton(ConfigScreen parent, int x, int y, int width, int height, Text message, PressAction onPress) {
        super(x, y, width, height, message, onPress);
        this.parent = parent;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        TextRenderer textRenderer = minecraftClient.textRenderer;
        DrawableHelper.fill(matrices, this.x, this.y, this.x + this.width, this.y + this.height, 0x44FFFFFF);

        if (VividConfig.Animation.ENABLED) {
            if (isMouseOver(mouseX, mouseY) && (this.parent.getFocused() == null || this.parent.getFocused() == this)) {
                hoverOpacity = Math.min(1F, VividConfig.Animation.SPEED + hoverOpacity);
            } else {
                hoverOpacity = Math.max(0F, hoverOpacity - VividConfig.Animation.SPEED);
            }
        } else {
            hoverOpacity = this.isMouseOver(mouseX, mouseY) ? 1F : 0F;
        }

        fill(matrices, x, y, x + width, y + getHeight(), 0xFFFFFFFF, hoverOpacity * 0.75F);
        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2F, this.y + (this.height - 8) / 2F, 0xFFFFFFFF, 1.25F * parent.getScale());
    }
}
