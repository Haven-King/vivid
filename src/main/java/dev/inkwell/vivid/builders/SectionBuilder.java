package dev.inkwell.vivid.builders;

import dev.inkwell.vivid.entry.base.ListEntry;
import dev.inkwell.vivid.util.Group;
import dev.inkwell.vivid.util.Quad;
import net.minecraft.text.MutableText;
import net.minecraft.util.Pair;

import java.util.function.Supplier;

public class SectionBuilder extends Group<Object> {
	public SectionBuilder(MutableText title) {
		super(title);
	}

	public <T> SectionBuilder add(MutableText name, Supplier<T> defaultValue, Supplier<T> value, ValueEntryBuilder<T> valueEntryBuilder) {
		this.add(new ValueBuilder<>(name, defaultValue, value, valueEntryBuilder));
		return this;
	}

	public SectionBuilder add(MutableText name, ListEntryBuilder entryBuilder) {
		this.add(new ListBuilder(name, entryBuilder));
		return this;
	}

	@SuppressWarnings("unchecked")
	public Group<ListEntry> build() {
		Group<ListEntry> section = new Group<>(this.name);

		for (Object group : this) {
			if (group instanceof ValueBuilder) {
				ValueBuilder<?> valueBuilder = (ValueBuilder<?>) group;
				section.add(
						((ValueBuilder<?>) group).getD().build(valueBuilder.getA(), (Supplier<Object>) valueBuilder.getB(), valueBuilder.getC().get())
				);
			} else if (group instanceof ListBuilder) {
				section.add(((ListBuilder) group).getRight().build(((ListBuilder) group).getLeft()));
			}
		}

		return section;
	}

	static class ListBuilder extends Pair<MutableText, ListEntryBuilder> {
		public ListBuilder(MutableText left, ListEntryBuilder right) {
			super(left, right);
		}
	}

	static class ValueBuilder<T> extends Quad<MutableText, Supplier<T>, Supplier<T>, ValueEntryBuilder<T>> {
		public ValueBuilder(MutableText mutableText, Supplier<T> tSupplier, Supplier<T> tSupplier2, ValueEntryBuilder<T> valueEntryBuilder) {
			super(mutableText, tSupplier, tSupplier2, valueEntryBuilder);
		}
	}
}
