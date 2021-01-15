package dev.inkwell.vivid.builders;

import dev.inkwell.vivid.screen.ConfigScreen;
import dev.inkwell.vivid.util.Group;
import dev.inkwell.vivid.widgets.LabelComponent;
import dev.inkwell.vivid.widgets.SpacerComponent;
import dev.inkwell.vivid.widgets.WidgetComponent;
import dev.inkwell.vivid.widgets.value.SectionHeaderComponent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class SectionBuilder extends Group<SectionBuilder.WidgetBuilder> {
	public SectionBuilder(MutableText title) {
		super(title);
	}

	public SectionBuilder addTooltip(Text tooltip) {
		this.add(tooltip);

		return this;
	}

	public Group<WidgetComponent> build(ConfigScreen parent, int contentLeft, int contentWidth, int y, Integer index) {
		Group<WidgetComponent> section = new Group<>(this.name);
		section.addAll(this.tooltips);

		int offset = y;

		if (!this.getName().getString().isEmpty()) {
			WidgetComponent component = new SectionHeaderComponent(parent, contentLeft, offset, contentWidth, (int) (45 * parent.getScale()), this.name, false).withColor(parent.getStyle().sectionColor);
			section.add(component);
			offset += component.getHeight();
		}

		for (WidgetBuilder builder : this) {
			WidgetComponent component = builder.build(parent, contentWidth, contentLeft, offset, index++);
			section.add(component);
			offset += component.getHeight();
		}

		return section;
	}

	@FunctionalInterface
	public interface WidgetBuilder {
		WidgetComponent build(ConfigScreen parent, int width, int x, int y, int index);
	}
}
