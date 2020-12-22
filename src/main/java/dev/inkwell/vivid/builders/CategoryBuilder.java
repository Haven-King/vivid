package dev.inkwell.vivid.builders;

import dev.inkwell.vivid.entry.base.ListEntry;
import dev.inkwell.vivid.util.Group;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class CategoryBuilder extends Group<SectionBuilder> {
	public CategoryBuilder(MutableText name) {
		super(name);
	}

	public CategoryBuilder addTooltip(Text tooltip) {
		this.add(tooltip);

		return this;
	}

	public SectionBuilder addSection(SectionBuilder sectionBuilder) {
		this.add(sectionBuilder);
		return sectionBuilder;
	}

	public SectionBuilder addSection(MutableText name) {
		return this.addSection(new SectionBuilder(name));
	}

	public Group<Group<ListEntry>> build() {
		Group<Group<ListEntry>> category = new Group<>(this.name);
		category.addAll(this.tooltips);

		for (SectionBuilder sectionBuilder : this) {
			category.add(sectionBuilder.build());
		}

		return category;
	}
}
