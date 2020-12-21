package dev.inkwell.vivid.entry;

import dev.inkwell.vivid.entry.base.DropdownEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;

import java.util.function.Supplier;

public class StringDropdown extends DropdownEntry<String> {
	public StringDropdown(MutableText name, Supplier<?> defaultValue, Object value) {
		super(name, defaultValue, value);
	}

	@Override
	protected MutableText valueOf(String value) {
		return new TranslatableText(value);
	}
}
