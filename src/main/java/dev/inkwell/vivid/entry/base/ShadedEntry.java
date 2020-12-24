package dev.inkwell.vivid.entry.base;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static dev.inkwell.vivid.Vivid.BLUR;

public abstract class ShadedEntry<T> extends ValueEntry<T> {
	protected boolean enabled = false;

	public ShadedEntry(MutableText name, Supplier<?> defaultValue, Consumer<?> saveConsumer, Object value, EntryType entryType) {
		super(name, defaultValue, saveConsumer, value, entryType);
	}

	protected abstract double getShadeHeight();

	@Override
	public void renderContents(MatrixStack matrices, int index, int width, int y, int mouseX, int mouseY, float delta) {
		if (this.isShaded()) {
			fill(matrices, -10000, -10000, 10000, 10000, 0, 0.5F);
			BLUR.setUniformValue("Start", 0F, 0F);
			BLUR.setUniformValue("End", 1F, 1F);
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
