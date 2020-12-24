package dev.inkwell.vivid.entry;

import dev.inkwell.vivid.entry.base.DropdownEntry;
import dev.inkwell.vivid.entry.base.EntryType;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class StringDropdown extends DropdownEntry<String> {
	public StringDropdown(MutableText name, Supplier<?> defaultValue, Consumer<Object> saveConsumer, Object value, EntryType entryType) {
		super(name, defaultValue, saveConsumer, value, entryType);
	}

	@Override
	protected MutableText valueOf(String value) {
		return new TranslatableText(value);
	}
}
