package dev.inkwell.vivid.entry.base;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;

import java.util.function.Supplier;

import static dev.inkwell.vivid.Vivid.BLUR;

public abstract class ShadedEntry<T> extends ValueEntry<T> {
	protected boolean enabled = false;

	public ShadedEntry(MutableText name, Supplier<?> defaultValue, Object value) {
		super(name, defaultValue, value);
	}

	protected abstract double getShadeHeight();

	@Override
	public void render(MatrixStack matrices, int index, int width, int y, int mouseX, int mouseY, float delta) {
		super.render(matrices, index, width, y, mouseX, mouseY, delta);

		if (this.isShaded()) {
			fill(matrices, -10000, -10000, 10000, 10000, 0, 0.5F);
			BLUR.setUniformValue("Progress", 1F);
			BLUR.setUniformValue("Radius", 5F);
			BLUR.render(delta);
		}
	}

	protected final void setShade(boolean enabled) {
		this.enabled = enabled;
	}

	protected final boolean isShaded() {
		return enabled;
	}
}
