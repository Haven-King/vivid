package dev.inkwell.vivid.entry.sliders;

import net.minecraft.text.MutableText;

import java.util.function.Supplier;

public class DoubleSlider extends SliderEntry<Double> {
	public DoubleSlider(MutableText name, Supplier<?> defaultValue, Object value) {
		super(name, defaultValue, value);
	}

	@Override
	protected float getPercentage() {
		return (float) ((this.getValue() - getMin()) / (float) (getMax() - getMin()));
	}

	@Override
	protected Double subtract(Double left, Double right) {
		return left - right;
	}

	@Override
	protected Double add(Double left, Double right) {
		return left + right;
	}

	@Override
	protected Double multiply(Double d1, double d2) {
		return d1 * d2;
	}

	@Override
	protected String stringValue() {
		return String.format("%.2f", this.getValue());
	}
}
