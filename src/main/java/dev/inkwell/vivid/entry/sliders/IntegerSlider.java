package dev.inkwell.vivid.entry.sliders;

import net.minecraft.text.MutableText;

import java.util.function.Supplier;

public class IntegerSlider extends SliderEntry<Integer> {
	public IntegerSlider(MutableText name, Supplier<?> defaultValue, Object value) {
		super(name, defaultValue, value);
	}

	@Override
	protected float getPercentage() {
		return ((this.getValue() - getMin()) / (float) (getMax() - getMin()));
	}

	@Override
	protected Integer subtract(Integer left, Integer right) {
		return left - right;
	}

	@Override
	protected Integer add(Integer left, Integer right) {
		return left + right;
	}

	@Override
	protected Integer multiply(Integer integer, double d) {
		return (int) (integer * d);
	}

	@Override
	protected String stringValue() {
		return String.valueOf(this.getValue());
	}
}
