package dev.inkwell.vivid.entry.base;

import dev.inkwell.vivid.DrawableExtensions;
import dev.inkwell.vivid.VividConfig;
import dev.inkwell.vivid.constraints.Constraint;
import dev.inkwell.vivid.screen.ConfigScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public abstract class ListEntry implements Element, DrawableExtensions {
	private final MutableText name;
	private final List<Text> tooltips;
	protected ConfigScreen parent;
	private int x = 0;
	protected int y;
	protected int width;
	private boolean focused;
	protected float hoverOpacity = 0F;

	protected ListEntry(MutableText name) {
		this.name = name;
		tooltips = new ArrayList<>();
	}

	public abstract int getHeight();

	public boolean holdsFocus() {
		return false;
	}

	public Text getName() {
		return this.name;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		return mouseX >= (double)this.x + this.width / 2F && mouseY >= (double)this.y && mouseX < (double)(this.x + this.width) && mouseY < (double)(this.y + this.getHeight());
	}

	public boolean isMouseOver(double mouseX, double mouseY) {
		return mouseX >= (double)this.x && mouseY >= (double)this.y && mouseX < (double)(this.x + this.width) && mouseY < (double)(this.y + this.getHeight());
	}

	public boolean isFocused() {
		return this.focused;
	}

	public void setFocused(boolean focused) {
		this.focused = focused;
	}

	public void render(MatrixStack matrices, int index, int width, int y, int mouseX, int mouseY, float delta) {
		this.y = y;
		this.width = width;

		int color1 = index % 2 == 0 ? 0x44FFFFFF : 0x22888888;
		int color2 = index % 2 == 0 ? 0x22FFFFFF : 0x11888888;

		if (VividConfig.Animation.enabled) {
			if (isMouseOver(mouseX, mouseY) && (this.parent.getFocused() == null || this.parent.getFocused() == this)) {
				hoverOpacity = Math.min(1F, VividConfig.Animation.speed + hoverOpacity);
			} else {
				hoverOpacity = Math.max(0F, hoverOpacity - VividConfig.Animation.speed);
			}
		} else {
			hoverOpacity = this.isMouseOver(mouseX, mouseY) ? 1F : 0F;
		}

		if (this instanceof ValueEntry && ((ValueEntry<?>) this).hasError()) {
			color1 = 0x77FF0000;
		}

		DrawableHelper.fill(matrices, 0, y, width / 2, y + getHeight(), color1);
		DrawableHelper.fill(matrices, width / 2, y, width, y + getHeight(), color2);

		renderHighlight(matrices, width, y, 0xFFFFFFFF);
		TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		this.draw(matrices, textRenderer, this.getName(), 3, y + (int) ((this.getHeight() - textRenderer.fontHeight * parent.getScale()) / 2F) - 1, 0xFFFFFFFF, parent.getScale());
	}

	protected void renderHighlight(MatrixStack matrices, int width, int y, int color) {
		fill(matrices, 0, y, width, y + getHeight(), 0xFFFFFFFF, hoverOpacity * 0.75F);
	}

	public void init(ConfigScreen parent) {
		this.parent = parent;
	}

	protected int getX() {
		return this.x;
	}

	public final void addTooltips(List<Text> tooltips) {
		this.tooltips.addAll(tooltips);
	}

	public final void addTooltipsToList(List<Text> tooltips) {
		tooltips.addAll(this.tooltips);

		if (this instanceof Constraint) {
			((Constraint) this).addConstraintTooltips(tooltips);
		}
	}
}
