package dev.monarkhes.vivid.builders;

import dev.monarkhes.vivid.Category;
import dev.monarkhes.vivid.screen.ConfigScreen;
import dev.monarkhes.vivid.util.Group;
import dev.monarkhes.vivid.widgets.SpacerComponent;
import dev.monarkhes.vivid.widgets.WidgetComponent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.function.BooleanSupplier;

public class CategoryBuilder extends Group<SectionBuilder> {
	private Runnable saveCallback = () -> {};
	private BooleanSupplier condition = () -> true;

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

	public CategoryBuilder setSaveCallback(Runnable saveCallback) {
		this.saveCallback = saveCallback;
		return this;
	}

	public CategoryBuilder setCondition(BooleanSupplier condition) {
		this.condition = condition;
		return this;
	}

	public boolean shouldShow() {
		return this.condition.getAsBoolean();
	}

	public Category build(ConfigScreen parent, int contentLeft, int contentWidth, int headerOffset) {
		Category category = new Category(this.name, this.saveCallback);
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
