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

public class FloatEntry extends TextEntry<Float> implements Bounded<Float> {
	private Float min = null;
	private Float max = null;

	public FloatEntry(MutableText name, Supplier<?> defaultValue, Object value) {
		super(name, defaultValue, value);
	}

	@Override
	protected String valueOf(Float value) {
		return String.valueOf(value);
	}

	@Override
	protected Float emptyValue() {
		return 0F;
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
	public void init(ConfigScreen parent) {
		super.init(parent);
		this.setPredicate(string -> {
			try {
				Double.parseDouble(string);
				return true;
			} catch (NumberFormatException ignored) {
				return false;
			}
		});
	}

	@Override
	protected Optional<Float> parse(String value) {
		try {
			return Optional.of(Float.valueOf(value));
		} catch (NumberFormatException ignored) {
			return Optional.empty();
		}
	}

	@Override
	public boolean isWithinBounds(Float value) {
		return (min == null || value >= min) && (max == null || value <= max);
	}

	@Override
	public void setMin(@Nullable Float min) {
		this.min = min;
	}

	@Override
	public void setMax(@Nullable Float max) {
		this.max = max;
	}

	@Override
	public @Nullable Float getMin() {
		return min;
	}

	@Override
	public @Nullable Float getMax() {
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
