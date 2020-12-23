package dev.inkwell.vivid.entry.base;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class ValueEntry<T> extends ListEntry {
	protected final Supplier<T> defaultValue;
	private Consumer<T> changedListener = t -> {};

	private T value;

	@SuppressWarnings("unchecked")
	public ValueEntry(MutableText name, Supplier<?> defaultValue, Object value) {
		super(name);
		this.defaultValue = (Supplier<T>) defaultValue;
		this.value = (T) value;
	}

	protected final T getValue() {
		return value;
	}

	protected void setValue(T value) {
		if (value != this.value) {
			T oldValue = this.value;
			this.value = value;

			if (this.hasError()) {
				this.value = oldValue;
			} else {
				this.changedListener.accept(value);
			}
		}
	}

	@Override
	protected void renderHighlight(MatrixStack matrices, int width, int y, int color) {
		int textColor = this.hasError() ? 0xFFFFAAAA : color;
		super.renderHighlight(matrices, width, y, textColor);
	}

	@Override
	public int getHeight() {
		return (int) (30 * parent.getScale());
	}

	public final ValueEntry<T> setSaveListener(Consumer<T> listener) {
		this.changedListener = listener;
		return this;
	}

	public abstract boolean hasError();
}
