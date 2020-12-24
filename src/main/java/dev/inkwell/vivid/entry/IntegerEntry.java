package dev.inkwell.vivid.entry;

import dev.inkwell.vivid.constraints.Bounded;
import dev.inkwell.vivid.entry.base.EntryType;
import dev.inkwell.vivid.entry.base.TextEntry;
import dev.inkwell.vivid.screen.ConfigScreen;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class IntegerEntry extends TextEntry<Integer> implements Bounded<Integer> {
	private Integer min = null;
	private Integer max = null;

	public IntegerEntry(MutableText name, Supplier<?> defaultValue, Consumer<?> saveConsumer, Object value, EntryType entryType) {
		super(name, defaultValue, saveConsumer, value, entryType);
	}

	@Override
	protected String valueOf(Integer value) {
		return String.valueOf(value);
	}

	@Override
	protected Integer emptyValue() {
		return 0;
	}

	@Override
	public void init(ConfigScreen parent) {
		super.init(parent);
		this.setPredicate(string -> {
			try {
				long value = Long.parseLong(string);
				return value <= Integer.MAX_VALUE && value >= Integer.MIN_VALUE;
			} catch (NumberFormatException ignored) {
				return false;
			}
		});
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
	protected Optional<Integer> parse(String value) {
		try {
			return Optional.of(Integer.valueOf(value));
		} catch (NumberFormatException ignored) {
			return Optional.empty();
		}
	}

	@Override
	public boolean isWithinBounds(Integer value) {
		return (min == null || value >= min) && (max == null || value <= max);
	}

	@Override
	public void setMin(@Nullable Integer min) {
		this.min = min;
	}

	@Override
	public void setMax(@Nullable Integer max) {
		this.max = max;
	}

	@Override
	public @Nullable Integer getMin() {
		return min;
	}

	@Override
	public @Nullable Integer getMax() {
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
