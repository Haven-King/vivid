package dev.inkwell.vivid.entry;

import dev.inkwell.vivid.entry.base.ValueEntry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;

import java.util.function.Supplier;

public class BooleanEntry extends ValueEntry<Boolean> {
	public BooleanEntry(MutableText name, Supplier<?> defaultValue, Object value) {
		super(name, defaultValue, value);
	}

	@Override
	public void render(MatrixStack matrices, int index, int width, int y, int mouseX, int mouseY, float delta) {
		super.render(matrices, index, width, y, mouseX, mouseY, delta);

		matrices.push();

		float x1 = width * 0.75F - 5, y1 = getHeight() / 2F - 5 + y, x2 = width * 0.75F + 5, y2 = getHeight() / 2F + 5 + y;

		fill(matrices, x1, y1, x2, y2, 0xFFFFFFFF, 0.4F);

		if (this.getValue()) {
			float padding = 2;
			fill(matrices, x1 + padding, y1 + padding, x2 - padding, y2 - padding, 0xFFFFFFFF, 0.8F);
		}

		matrices.pop();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		boolean isWithinBounds = super.mouseClicked(mouseX, mouseY, button);

		if (isWithinBounds) {
			this.setValue(!this.getValue());
		}

		return isWithinBounds;
	}

	@Override
	public boolean hasError() {
		return false;
	}
}
