package dev.inkwell.vivid.builders;

import dev.inkwell.vivid.entry.base.ListEntry;
import dev.inkwell.vivid.screen.ConfigScreen;
import dev.inkwell.vivid.screen.ScreenStyle;
import dev.inkwell.vivid.util.Group;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.MutableText;

import java.util.ArrayList;
import java.util.List;

public class ConfigScreenBuilder {
	private final List<CategoryBuilder> children = new ArrayList<>();

	private ScreenStyle style = ScreenStyle.DEFAULT;

	public CategoryBuilder startCategory(MutableText name) {
		CategoryBuilder category = new CategoryBuilder(name);
		children.add(category);

		return category;
	}

	public ConfigScreenBuilder withStyle(ScreenStyle style) {
		this.style = style;
		return this;
	}

	public ConfigScreen build(Screen parent) {
		List<Group<Group<ListEntry>>> categories = new ArrayList<>();

		for (CategoryBuilder categoryBuilder : children) {
			categories.add(categoryBuilder.build());
		}

		return new ConfigScreen(parent, categories).withStyle(this.style);
	}
}
