package dev.inkwell.vivid.builders;

import dev.inkwell.vivid.screen.ConfigScreen;
import dev.inkwell.vivid.util.Group;
import dev.inkwell.vivid.widgets.SpacerComponent;
import dev.inkwell.vivid.widgets.WidgetComponent;
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

	public Group<Group<WidgetComponent>> build(ConfigScreen parent, int contentLeft, int contentWidth, int headerOffset) {
		Group<Group<WidgetComponent>> category = new Group<>(this.name);
		category.addAll(this.tooltips);

		int y = headerOffset;

		Integer index = 0;
		for (int i = 0; i < this.size(); ++i) {
			Group<WidgetComponent> section = this.get(i).build(parent, contentLeft, contentWidth, y, index);
			category.add(section);

			if (i == this.size() - 1) {
				int offset = y;

				for (WidgetComponent component : section) {
					y += component.getHeight();
				}

				section.add(new SpacerComponent(parent, contentLeft, offset, contentWidth, 15));
			}

			for (WidgetComponent component : section) {
				y += component.getHeight();
			}
		}

		return category;
	}
}
