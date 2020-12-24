package dev.inkwell.vivid.entry.base;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class ValueEntry<T> extends ListEntry {
	protected final Supplier<T> defaultValue;
	protected final EntryType entryType;
	private Consumer<T> changedListener = t -> {};
	private Consumer<T> saveConsumer;

	private final T initialValue;
	private T value;

	@SuppressWarnings("unchecked")
	public ValueEntry(MutableText name, Supplier<?> defaultValue, Consumer<?> saveConsumer, Object value, EntryType entryType) {
		super(name);
		this.defaultValue = (Supplier<T>) defaultValue;
		this.initialValue = (T) value;
		this.value = (T) value;
		this.saveConsumer = (Consumer<T>) saveConsumer;
		this.entryType = entryType;
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
	protected void renderHighlight(MatrixStack matrices, int x, int width, int y, int color) {
		int textColor = this.hasError() ? 0xFFFFAAAA : color;
		super.renderHighlight(matrices, x, width, y, textColor);
	}

	@Override
	public int getHeight() {
		return (int) (30 * parent.getScale());
	}

	public final ValueEntry<T> setChangedListener(Consumer listener) {
		this.changedListener = listener;
		return this;
	}

	public final ValueEntry<T> setSaveListener(Consumer listener) {
		this.saveConsumer = listener;
		return this;
	}

	public void save() {
		this.saveConsumer.accept(this.getValue());
	}

	public boolean hasChanged() {
		return !this.initialValue.equals(this.value);
	}

	public String getDefaultValueAsString() {
		return this.defaultValue.get().toString();
	}

	public abstract boolean hasError();
}
