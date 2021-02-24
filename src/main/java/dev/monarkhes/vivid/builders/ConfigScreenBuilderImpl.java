package dev.monarkhes.vivid.builders;

import dev.monarkhes.vivid.Category;
import dev.monarkhes.vivid.screen.ConfigScreen;
import dev.monarkhes.vivid.screen.ScreenStyle;
import dev.monarkhes.vivid.util.Group;
import dev.monarkhes.vivid.widgets.WidgetComponent;
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
	public List<Category> build(ConfigScreen parent, int contentLeft, int contentWidth, int y) {
		List<Category> categories = new ArrayList<>();

		for (CategoryBuilder categoryBuilder : children) {
			if (categoryBuilder.shouldShow()) {
				categories.add(categoryBuilder.build(parent, contentLeft, contentWidth, y));
			}
		}

		return categories;
	}
}
