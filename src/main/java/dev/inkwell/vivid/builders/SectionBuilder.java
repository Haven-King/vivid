package dev.inkwell.vivid.builders;

import dev.inkwell.vivid.entry.base.ListEntry;
import dev.inkwell.vivid.util.Group;
import dev.inkwell.vivid.util.Quintuple;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SectionBuilder extends Group<Object> {
	public SectionBuilder(MutableText title) {
		super(title);
	}

	public SectionBuilder addTooltip(Text tooltip) {
		this.add(tooltip);

		return this;
	}

	public <T> SectionBuilder addConfigEntry(MutableText name, Supplier defaultValue, Supplier value, Consumer saveConsumer, ValueEntryBuilder valueEntryBuilder) {
		this.add(new ValueBuilder<>(name, defaultValue, value, saveConsumer, valueEntryBuilder));
		return this;
	}

	public SectionBuilder addListEntry(MutableText name, ListEntryBuilder entryBuilder) {
		this.add(new ListBuilder(name, entryBuilder));
		return this;
	}

	@SuppressWarnings("unchecked")
	public Group<ListEntry> build() {
		Group<ListEntry> section = new Group<>(this.name);
		section.addAll(this.tooltips);

		for (Object group : this) {
			if (group instanceof ValueBuilder) {
				ValueBuilder<?> valueBuilder = (ValueBuilder<?>) group;
				section.add(
						((ValueBuilder<?>) group).getE().build(valueBuilder.getA(), (Supplier<Object>) valueBuilder.getB(), (Consumer<Object>) valueBuilder.getD(), valueBuilder.getC().get())
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

	static class ValueBuilder<T> extends Quintuple<MutableText, Supplier<T>, Supplier<T>, Consumer<T>, ValueEntryBuilder<T>> {
		public ValueBuilder(MutableText mutableText, Supplier<T> tSupplier, Supplier<T> tSupplier2, Consumer<T> saveConsumer, ValueEntryBuilder<T> valueEntryBuilder) {
			super(mutableText, tSupplier, tSupplier2, saveConsumer, valueEntryBuilder);
		}
	}
}
