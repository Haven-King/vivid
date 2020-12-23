package dev.inkwell.vivid.entry;

import dev.inkwell.vivid.constraints.Bounded;
import dev.inkwell.vivid.entry.base.TextEntry;
import dev.inkwell.vivid.screen.ConfigScreen;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class DoubleEntry extends TextEntry<Double> implements Bounded<Double> {
	private Double min = null;
	private Double max = null;

	public DoubleEntry(MutableText name, Supplier<Double> defaultValue, Double value) {
		super(name, defaultValue, value);
	}

	@Override
	protected String valueOf(Double value) {
		return String.valueOf(value);
	}

	@Override
	protected void renderValue(MatrixStack matrices, TextRenderer textRenderer, String text, int x, int y, int color, float scale) {
		super.renderValue(
				matrices,
				textRenderer,
				text.isEmpty() ? "0" : text,
				text.isEmpty() ? (int) (x - textRenderer.getWidth("0") * parent.getScale()) : x,
				y,
				color,
				scale);
	}

	@Override
	protected Double emptyValue() {
		return 0D;
	}

	@Override
	public void init(ConfigScreen parent) {
		super.init(parent);
		this.setPredicate(string -> {
			if (string.isEmpty()) {
				return true;
			}

			try {
				Double.parseDouble(string);
				return true;
			} catch (NumberFormatException ignored) {
				return false;
			}
		});
	}

	@Override
	protected Optional<Double> parse(String value) {
		try {
			return Optional.of(Double.valueOf(value));
		} catch (NumberFormatException ignored) {
			return Optional.empty();
		}
	}

	@Override
	public boolean isWithinBounds(Double value) {
		return (min == null || value >= min) && (max == null || value <= max);
	}

	@Override
	public void setMin(@Nullable Double min) {
		this.min = min;
	}

	@Override
	public void setMax(@Nullable Double max) {
		this.max = max;
	}

	@Override
	public @Nullable Double getMin() {
		return min;
	}

	@Override
	public @Nullable Double getMax() {
		return max;
	}

	@Override
	public boolean passes() {
		return isWithinBounds(this.getValue());
	}

	@Override
	public void addConstraintTooltips(List<Text> tooltips) {
		Bounded.super.addConstraintTooltips(tooltips);
	}
}
