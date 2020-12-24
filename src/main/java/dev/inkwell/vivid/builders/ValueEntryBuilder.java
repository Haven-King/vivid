package dev.inkwell.vivid.builders;

import dev.inkwell.vivid.entry.base.EntryType;
import dev.inkwell.vivid.entry.base.ValueEntry;
import net.minecraft.text.MutableText;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface ValueEntryBuilder<T> extends EntryBuilder {
	ValueEntry<T> build(MutableText name, Supplier<Object> defaultValue, Consumer<Object> saveAction, Object value, EntryType entryType);

	default Type getType() {
		return Type.VALUE;
	}
}
