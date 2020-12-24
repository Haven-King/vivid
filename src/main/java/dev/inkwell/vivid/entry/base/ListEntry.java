package dev.inkwell.vivid.entry.base;

import dev.inkwell.vivid.DrawableExtensions;
import dev.inkwell.vivid.VividConfig;
import dev.inkwell.vivid.constraints.Constraint;
import dev.inkwell.vivid.entry.ArrayEntry;
import dev.inkwell.vivid.screen.ConfigScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.List;

public abstract class ListEntry implements Element, DrawableExtensions {
	private final MutableText name;
	private final List<Text> tooltips;
	protected ConfigScreen parent;
	private int x = 0;
	protected int y;
	protected int width;
	protected int contentWidth;
	protected int contentLeft;
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
		return mouseX >= (double)this.contentLeft && mouseY >= (double)this.y && mouseX < (double)(this.contentLeft + this.contentWidth) && mouseY < (double)(this.y + this.getHeight());
	}

	public boolean isMouseOver(double mouseX, double mouseY) {
		if (this instanceof ValueEntry && ((ValueEntry<?>) this).entryType == EntryType.ARRAY) {
			return mouseX >= (double)this.contentLeft && mouseY >= (double)this.y && mouseX < (double)(this.contentLeft + this.contentWidth) && mouseY < (double)(this.y + this.getHeight());
		} else {
			return mouseX >= (double) this.x && mouseY >= (double) this.y && mouseX < (double) (this.x + this.width) && mouseY < (double) (this.y + this.getHeight());
		}
	}

	public boolean isFocused() {
		return this.focused;
	}

	public void setFocused(boolean focused) {
		this.focused = focused;
	}

	public final void render(MatrixStack matrices, int index, int width, int y, int mouseX, int mouseY, float delta) {
		this.y = y;
		this.width = width;

		int color1 = index % 2 == 0 ? 0x44FFFFFF : 0x22888888;
		int color2 = index % 2 == 0 ? 0x22FFFFFF : 0x11888888;

		if (this instanceof ValueEntry && ((ValueEntry<?>) this).hasError()) {
			color1 = 0x77FF0000;
		}

		boolean split = true;
		contentWidth = width / 2;
		contentLeft = width / 2;
		int highlightLeft = 0;

		if (this instanceof ArrayEntry.AddButton) {
			split = false;
			contentLeft = 0;
			contentWidth = width;
		} else if (this instanceof ValueEntry && ((ValueEntry<?>) this).entryType == EntryType.ARRAY) {
			split = false;
			contentLeft = (int) (30 * parent.getScale()) * 2;
			contentWidth = width - contentLeft;
			highlightLeft = (int) (30 * parent.getScale()) * 2;
		}

		this.updateHoverOpacity(mouseX, mouseY);

		if (split) {
			DrawableHelper.fill(matrices, 0, y, contentLeft, y + getHeight(), color1);
		}

		DrawableHelper.fill(matrices, contentLeft, y, width, y + getHeight(), color2);
		renderHighlight(matrices, width, highlightLeft, y, 0xFFFFFFFF);

		if (split) {
			TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
			this.draw(matrices, textRenderer, this.getName(), 3, y + (int) ((this.getHeight() - textRenderer.fontHeight * parent.getScale()) / 2F) - 1, 0xFFFFFFFF, parent.getScale());
		} else if (!(this instanceof ArrayEntry.AddButton)) {
			this.drawDragButton(matrices, y, mouseX, mouseY, delta);
//		this.drawDeleteButton(matrices, y, mouseX, mouseY, delta);
		}

		matrices.push();
		matrices.translate(contentLeft, 0, 0);
		this.renderContents(matrices, index, contentWidth, y, mouseX - contentLeft, mouseY, delta);
		matrices.pop();

	}

	private void drawDragButton(MatrixStack matrices, int y, int mouseX, int mouseY, float delta) {
		float size = 30 * parent.getScale();
		fill(matrices, 0, y, size, y + size, 0x22FFFFFF, 0.125F);
		TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		DrawableHelper.drawCenteredString(matrices, textRenderer, "☰", (int) (size / 2), y + (int) ((this.getHeight() - textRenderer.fontHeight * parent.getScale()) / 2), 0xFFFFFFFF);
	}

	private void drawDeleteButton(MatrixStack matrices, int y, int mouseX, int mouseY, float delta) {
		float size = 30 * parent.getScale();
		fill(matrices, size, y, size * 2, y + size, 0xFFFF4040, 0.5F);
	}

	protected abstract void renderContents(MatrixStack matrixStack, int index, int width, int y, int mouseX, int mouseY, float delta);

	protected void updateHoverOpacity(int mouseX, int mouseY) {
		if (VividConfig.Animation.ENABLED) {
			if (isMouseOver(mouseX, mouseY) && (this.parent.getFocused() == null || this.parent.getFocused() == this)) {
				hoverOpacity = Math.min(1F, VividConfig.Animation.SPEED + hoverOpacity);
			} else {
				hoverOpacity = Math.max(0F, hoverOpacity - VividConfig.Animation.SPEED);
			}
		} else {
			hoverOpacity = this.isMouseOver(mouseX, mouseY) ? 1F : 0F;
		}
	}

	protected void renderHighlight(MatrixStack matrices, int x, int width, int y, int color) {
		fill(matrices, x, y, width, y + getHeight(), 0xFFFFFFFF, hoverOpacity * 0.75F);
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

	public void addTooltipsToList(List<Text> tooltips) {
		tooltips.addAll(this.tooltips);

		if (this instanceof Constraint) {
			((Constraint) this).addConstraintTooltips(tooltips);
		}

		if (this instanceof ValueEntry) {
			ValueEntry<?> valueEntry = (ValueEntry<?>) this;
			tooltips.add(new TranslatableText("vivid.default", valueEntry.getDefaultValueAsString()));
		}
	}
}
