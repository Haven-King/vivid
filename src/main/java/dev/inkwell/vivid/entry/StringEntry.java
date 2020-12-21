package dev.inkwell.vivid.entry;

import dev.inkwell.vivid.entry.base.TextEntry;
import net.minecraft.text.MutableText;

import java.util.Optional;
import java.util.function.Supplier;

public class StringEntry extends TextEntry<String> {
	public StringEntry(MutableText name, Supplier<?> defaultValue, Object value) {
		super(name, defaultValue, value);
	}

	@Override
	protected String valueOf(String value) {
		return value;
	}

	@Override
	protected String emptyValue() {
		return "";
	}

	@Override
	protected Optional<String> parse(String value) {
		return Optional.of(value);
	}
}
