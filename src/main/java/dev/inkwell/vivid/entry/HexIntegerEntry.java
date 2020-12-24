package dev.inkwell.vivid.entry;

import dev.inkwell.vivid.entry.base.EntryType;
import dev.inkwell.vivid.entry.base.TextEntry;
import dev.inkwell.vivid.screen.ConfigScreen;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class HexIntegerEntry extends TextEntry<Integer> {
	public HexIntegerEntry(MutableText name, Supplier<?> defaultValue, Consumer<?> saveConsumer, Object value, EntryType entryType) {
		super(name, defaultValue, saveConsumer, value, entryType);
	}

	@Override
	protected void renderValue(MatrixStack matrices, TextRenderer textRenderer, String text, int x, int y, int color, float scale) {
		super.renderValue(matrices, textRenderer, text, x, y, this.getValue(), scale);

		StringBuilder builder = new StringBuilder("0x");
		for (int i = 0; i < 8 - text.length(); ++i) {
			builder.append('0');
		}

		String paddedZeroes = builder.toString();

		draw(matrices, textRenderer, paddedZeroes, x - (textRenderer.getWidth(paddedZeroes) / 2), y, this.getValue(), parent.getScale());
	}

	@Override
	protected String valueOf(Integer value) {
		return Integer.toHexString(value);
	}

	@Override
	protected Integer emptyValue() {
		return 0;
	}

	@Override
	public void init(ConfigScreen parent) {
		super.init(parent);
		this.setPredicate(string -> string.matches("^[0-9A-Fa-f]*$") && string.length() <= 8);
	}

	@Override
	protected Optional<Integer> parse(String value) {
		try {
			return Optional.of(Long.valueOf(value, 16).intValue());
		} catch (NumberFormatException ignored) {
			return Optional.empty();
		}
	}

	@Override
	public boolean passes() {
		return true;
	}
}
