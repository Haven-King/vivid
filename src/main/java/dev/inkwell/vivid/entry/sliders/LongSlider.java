package dev.inkwell.vivid.entry.sliders;

import net.minecraft.text.MutableText;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class LongSlider extends SliderEntry<Long> {
	public LongSlider(MutableText name, Supplier<?> defaultValue, Consumer<?> saveConsumer, Object value) {
		super(name, defaultValue, saveConsumer, value);
	}

	@Override
	protected float getPercentage() {
		return ((this.getValue() - getMin()) / (float) (getMax() - getMin()));
	}

	@Override
	protected Long subtract(Long left, Long right) {
		return left - right;
	}

	@Override
	protected Long add(Long left, Long right) {
		return left + right;
	}

	@Override
	protected Long multiply(Long l, double d) {
		return (long) (l * d);
	}

	@Override
	protected String stringValue() {
		return String.valueOf(this.getValue());
	}
}
