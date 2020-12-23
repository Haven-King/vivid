package dev.inkwell.vivid.entry;

import dev.inkwell.vivid.constraints.Bounded;
import dev.inkwell.vivid.entry.base.TextEntry;
import dev.inkwell.vivid.screen.ConfigScreen;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class LongEntry extends TextEntry<Long> implements Bounded<Long> {
	private Long min = null;
	private Long max = null;

	public LongEntry(MutableText name, Supplier<?> defaultValue, Object value) {
		super(name, defaultValue, value);
	}

	@Override
	protected String valueOf(Long value) {
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
	protected Long emptyValue() {
		return 0L;
	}

	@Override
	public void init(ConfigScreen parent) {
		super.init(parent);
		this.setPredicate(string -> {
			try {
				Long.parseLong(string);
				return true;
			} catch (NumberFormatException ignored) {
				return false;
			}
		});
	}

	@Override
	protected Optional<Long> parse(String value) {
		try {
			return Optional.of(Long.valueOf(value));
		} catch (NumberFormatException ignored) {
			return Optional.empty();
		}
	}

	@Override
	public boolean isWithinBounds(Long value) {
		return (min == null || value >= min) && (max == null || value <= max);
	}

	@Override
	public void setMin(Long min) {
		this.min = min;
	}

	@Override
	public void setMax(Long max) {
		this.max = max;
	}

	@Override
	public Long getMin() {
		return min;
	}

	@Override
	public Long getMax() {
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
