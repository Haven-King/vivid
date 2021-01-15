package dev.inkwell.vivid.builders;

import dev.inkwell.vivid.screen.ConfigScreen;
import dev.inkwell.vivid.screen.ScreenStyle;
import dev.inkwell.vivid.util.Group;
import dev.inkwell.vivid.widgets.WidgetComponent;
import net.minecraft.text.MutableText;

import java.util.ArrayList;
import java.util.List;

public class ConfigScreenBuilderImpl implements ConfigScreenBuilder {
	private final List<CategoryBuilder> children = new ArrayList<>();

	private ScreenStyle style = ScreenStyle.DEFAULT;

	public CategoryBuilder startCategory(MutableText name) {
		CategoryBuilder category = new CategoryBuilder(name);
		children.add(category);

		return category;
	}

	public ConfigScreenBuilderImpl withStyle(ScreenStyle style) {
		this.style = style;
		return this;
	}

	@Override
	public List<Group<Group<WidgetComponent>>> build(ConfigScreen parent, int contentLeft, int contentWidth, int y) {
		List<Group<Group<WidgetComponent>>> categories = new ArrayList<>();

		for (CategoryBuilder categoryBuilder : children) {
			categories.add(categoryBuilder.build(parent, contentLeft, contentWidth, y));
		}

		return categories;
	}
}
