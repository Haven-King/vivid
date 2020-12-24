package dev.inkwell.vivid.entry.sliders;

import dev.inkwell.vivid.entry.base.EntryType;
import net.minecraft.text.MutableText;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FloatSlider extends SliderEntry<Float> {
	public FloatSlider(MutableText name, Supplier<?> defaultValue, Consumer<?> saveConsumer, Object value, EntryType entryType) {
		super(name, defaultValue, saveConsumer, value, entryType);
	}

	@Override
	protected float getPercentage() {
		return ((this.getValue() - getMin()) / (getMax() - getMin()));
	}

	@Override
	protected Float subtract(Float left, Float right) {
		return left - right;
	}

	@Override
	protected Float add(Float left, Float right) {
		return left + right;
	}

	@Override
	protected Float multiply(Float f, double d) {
		return (float) (f * d);
	}

	@Override
	protected String stringValue() {
		return String.format("%.2f", this.getValue());
	}
}
