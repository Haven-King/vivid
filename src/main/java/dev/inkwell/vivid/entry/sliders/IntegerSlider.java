package dev.inkwell.vivid.entry.sliders;

import net.minecraft.text.MutableText;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class IntegerSlider extends SliderEntry<Integer> {
	public IntegerSlider(MutableText name, Supplier<?> defaultValue, Consumer<?> saveConsumer, Object value) {
		super(name, defaultValue, saveConsumer, value);
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
