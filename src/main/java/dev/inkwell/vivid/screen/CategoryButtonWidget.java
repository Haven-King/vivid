package dev.inkwell.vivid.screen;

import dev.inkwell.vivid.DrawableExtensions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.List;

public class CategoryButtonWidget extends ButtonWidget implements DrawableExtensions {
	private final ConfigScreen parent;
	private final List<Text> tooltips;

	public CategoryButtonWidget(ConfigScreen parent, int x, int y, int width, int height, Text message, PressAction onPress, List<Text> tooltips) {
		super(x, y, width, height, message, onPress, null);
		this.parent = parent;
		this.tooltips = tooltips;
	}

	@Override
	public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

		MinecraftClient minecraftClient = MinecraftClient.getInstance();
		TextRenderer textRenderer = minecraftClient.textRenderer;
		Style textStyle = this.parent.getStyle().categoryColor.color(!this.active, this.hovered);

		int color = textStyle.getColor() != null
				? textStyle.getColor().getRgb()
				: 0xFFFFFFFF;

		MutableText text = this.getMessage().copy().styled(style -> textStyle);
		drawCenteredText(matrices, textRenderer, text, this.x + this.width / 2F, this.y + (this.height - 8) / 2F, color, parent.getScale());

		matrices.push();
		this.parent.getStyle().renderCategoryButtonDecorations(this, matrices, x, y, width, height);
		matrices.pop();

		if (this.isHovered()) {
			this.parent.addTooltips(this.tooltips);
		}
	}
}
