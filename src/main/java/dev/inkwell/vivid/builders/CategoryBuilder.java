package dev.inkwell.vivid.builders;

import dev.inkwell.vivid.util.Group;
import net.minecraft.text.MutableText;

public class CategoryBuilder extends Group<SectionBuilder> {
	public CategoryBuilder(MutableText name) {
		super(name);
	}

	public SectionBuilder addSection(SectionBuilder sectionBuilder) {
		this.add(sectionBuilder);
		return sectionBuilder;
	}

	public SectionBuilder addSection(MutableText name) {
		return this.addSection(new SectionBuilder(name));
	}
}
