package dev.inkwell.vivid.entry.base;

import dev.inkwell.vivid.VividConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;

import java.util.function.Supplier;

public abstract class DropdownEntry<T> extends ShadedEntry<T> {
	private T[] possibleValues;
	private float[] hoverOpacities;

	public DropdownEntry(MutableText name, Supplier<?> defaultValue, Object value) {
		super(name, defaultValue, value);
	}

	@Override
	protected double getShadeHeight() {
		return this.getHeight() * possibleValues.length;
	}

	public void setPossibleValues(T[] values) {
		this.possibleValues = values;
		this.hoverOpacities = new float[values.length];
	}

	protected abstract MutableText valueOf(T value);

	@Override
	public void render(MatrixStack matrices, int index, int width, int y, int mouseX, int mouseY, float delta) {
		super.render(matrices, index, width, y, mouseX, mouseY, delta);

		TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

		float x1 = width / 2F;

		if (!this.isShaded()) {
			MutableText text = this.valueOf(this.getValue());
			fill(matrices, x1, y, (float) width, y + getHeight(), 0xFFFFFFFF, 0.25F);
			drawCenteredText(matrices, textRenderer, text.styled(style -> style.withUnderline(true)), width * 0.75F, y + getHeight() * 0.24F, 0xFFFFFFFF, parent.getScale());
		}

		int j = 0;

		for (T value : possibleValues) {
			int y1 = y + (j) * this.getHeight();
			int y2 = y + (j + 1) * this.getHeight();


			if (VividConfig.Animation.enabled) {
				if (mouseY >= y1 && mouseY <= y2 && mouseX >= x1 && mouseX <= (float) width) {
					hoverOpacities[j] = Math.min(1F, VividConfig.Animation.speed + hoverOpacities[j]);
				} else {
					hoverOpacities[j] = Math.max(0F, hoverOpacities[j] - VividConfig.Animation.speed);
				}
			}

			if (this.isShaded()) {
				float alpha = j % 2 == 0 ? 0.125F : 0.25F;

				MutableText text = this.valueOf(value);
				fill(matrices, x1, y1, (float) width, y2, 0xFFFFFFFF, alpha);
				fill(matrices, x1, y1, (float) width, y2, 0xFFFFFFFF, hoverOpacities[j] * 0.75F);
				drawCenteredText(matrices, textRenderer, text, width * 0.75F, y1 + getHeight() / 4F, 0xFFFFFFFF, parent.getScale());
			}

			j++;
		}
	}

	@Override
	public boolean holdsFocus() {
		return true;
	}

	@Override
	public boolean hasError() {
		return false;
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return super.isMouseOver(mouseX, this.isShaded() ? ((mouseY - this.y) / possibleValues.length) + this.y : mouseY);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (!this.isShaded() && super.mouseClicked(mouseX, mouseY, button)) {
			this.setShade(true);
			return true;
		} else if (this.isShaded()) {
			boolean inBounds = super.mouseClicked(mouseX, ((mouseY - this.y) / possibleValues.length) + this.y, button);

			if (inBounds) {
				int i = (int) ((mouseY - this.y) / this.getHeight());
				this.setValue(possibleValues[i]);

				this.setShade(false);

				return true;
			}
		}

		return false;
	}

	@Override
	public void setFocused(boolean focused) {
		super.setFocused(focused);
		this.setShade(this.isShaded() && focused);
	}

}
