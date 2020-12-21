package dev.inkwell.vivid.builders;

import dev.inkwell.vivid.entry.base.ValueEntry;
import net.minecraft.text.MutableText;

import java.util.function.Supplier;

public interface ValueEntryBuilder<T> extends EntryBuilder {
	ValueEntry<T> build(MutableText name, Supplier<Object> defaultValue, Object value);

	default Type getType() {
		return Type.VALUE;
	}
}
