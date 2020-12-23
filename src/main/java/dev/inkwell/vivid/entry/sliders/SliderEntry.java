package dev.inkwell.vivid.entry.sliders;

import dev.inkwell.vivid.constraints.Bounded;
import dev.inkwell.vivid.entry.base.ValueEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.text.MutableText;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public abstract class SliderEntry<T extends Number> extends ValueEntry<T> implements Bounded<T> {
	private T min = null;
	private T max = null;

	public SliderEntry(MutableText name, Supplier<?> defaultValue, Object value) {
		super(name, defaultValue, value);
	}

	protected abstract float getPercentage();

	protected abstract T subtract(T left, T right);

	protected abstract T add(T left, T right);

	protected abstract T multiply(T t, double d);

	protected abstract String stringValue();

	@Override
	public void render(MatrixStack matrices, int index, int width, int y, int mouseX, int mouseY, float delta) {
		super.render(matrices, index, width, y, mouseX, mouseY, delta);
		String string = this.stringValue();
		TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		int dX = (int) (width - 3 - textRenderer.getWidth(string) * parent.getScale());
		draw(matrices, MinecraftClient.getInstance().textRenderer, string, dX, y + (int) ((this.getHeight() - textRenderer.fontHeight * parent.getScale()) / 2F) - 1, 0xFFFFFFFF, parent.getScale());

		int x1 = width / 2 + 6;
		int x2 = width - 2 - Math.max(textRenderer.getWidth(String.valueOf(getMin())), textRenderer.getWidth(String.valueOf(getMax())));
		int barWidth = x2 - x1;
		float y1 = y + getHeight() / 2F;

		line(matrices, x1, x2, y1, y1, 0xFFFFFFFF);

		float mark = x1 + getPercentage() * barWidth;

		matrices.push();
		matrices.translate(mark, y + getHeight() / 2F, 0);
		matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(45));
		fill(matrices, -1.5F, -1.5F, 1.5F, 1.5F, 0xFFFFFFFF, 1F);
		matrices.pop();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (super.mouseClicked(mouseX, mouseY, button)) {
			TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
			int x1 = width / 2 + 3;
			int x2 = width - 2 - Math.max(textRenderer.getWidth(String.valueOf(getMax())), textRenderer.getWidth(String.valueOf(getMin())));

			if (mouseX < x1) {
				this.setValue(this.getMin());
				return true;
			}

			if (mouseX > x2) {
				this.setValue(this.getMax());
				return true;
			}

			float mark = (float) (mouseX - x1) / (float) (x2 - x1);
			this.setValue(add((multiply(subtract(this.getMax(), this.getMin()), mark)), getMin()));

			return true;
		}

		return false;
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		return mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean hasError() {
		return false;
	}

	@Override
	public boolean isWithinBounds(T value) {
		return true;
	}

	@Override
	@SuppressWarnings("NullableProblems")
	public void setMin(@NotNull T min) {
		this.min = min;
	}

	@Override
	@SuppressWarnings("NullableProblems")
	public void setMax(@NotNull T max) {
		this.max = max;
	}

	@Override
	public @Nullable T getMin() {
		return min;
	}

	@Override
	public @Nullable T getMax() {
		return max;
	}

	@Override
	public boolean passes() {
		return isWithinBounds(this.getValue());
	}
}
