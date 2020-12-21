package dev.inkwell.vivid.entry;

import dev.inkwell.vivid.entry.base.ListEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;

public class ExternalButtonEntry extends ListEntry {
	private final Action onClick;
	public ExternalButtonEntry(MutableText name, Action onClick) {
		super(name);
		this.onClick = onClick;
	}

	@Override
	public int getHeight() {
		return 15;
	}

	@Override
	public void render(MatrixStack matrices, int index, int width, int y, int mouseX, int mouseY, float delta) {
		super.render(matrices, index, width, y, mouseX, mouseY, delta);

		int color = index % 2 == 0 ? 0x33FFFFFF : 0x33888888;

		DrawableHelper.fill(matrices, width / 2, y, width, y + getHeight(), color);
		fill(matrices, width / 2, y, width, y + getHeight(), 0xFFFFFFFF, hoverOpacity * 0.75F);

		TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		draw(matrices, textRenderer, ">", width - 6, y + (getHeight() - textRenderer.fontHeight) / 2 + 1, 0xFFFFFFFF, 0.5F);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		boolean bl = super.mouseClicked(mouseX, mouseY, button);

		if (bl && button == 0) {
			this.onClick.onClick(this.parent);
		}

		return bl;
	}

	public interface Action {
		void onClick(Screen parent);
	}
}