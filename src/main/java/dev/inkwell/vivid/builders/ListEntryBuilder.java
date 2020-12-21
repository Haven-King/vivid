package dev.inkwell.vivid.builders;

import dev.inkwell.vivid.entry.base.ListEntry;
import net.minecraft.text.MutableText;

public interface ListEntryBuilder extends EntryBuilder {
	ListEntry build(MutableText name);

	default Type getType() {
		return Type.LIST;
	}
}
