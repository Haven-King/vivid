package dev.inkwell.vivid;

import dev.inkwell.vivid.builders.CategoryBuilder;
import dev.inkwell.vivid.builders.ConfigScreenBuilder;
import dev.inkwell.vivid.builders.SectionBuilder;
import dev.inkwell.vivid.entry.*;
import dev.inkwell.vivid.entry.base.TextEntry;
import dev.inkwell.vivid.entry.sliders.DoubleSlider;
import dev.inkwell.vivid.entry.sliders.FloatSlider;
import dev.inkwell.vivid.entry.sliders.IntegerSlider;
import dev.inkwell.vivid.entry.sliders.LongSlider;
import dev.inkwell.vivid.screen.ScreenStyle;
import dev.inkwell.vivid.util.Enabled;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import net.fabricmc.fabric.api.util.TriState;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Direction;

import java.util.Random;

public class VividConfig implements ModMenuApi {
	public static class Animation {
		public static boolean enabled = true;
		public static float speed = 0.2F;
	}

	private final ConfigScreenBuilder builder;

	public VividConfig() {
		builder = new ConfigScreenBuilder().withStyle(ScreenStyle.NETHER);

		CategoryBuilder config = builder.startCategory(new TranslatableText("vivid.config"));

		SectionBuilder animation = config.addSection(new TranslatableText("vivid.config.animation"));
		animation.addConfigEntry(new TranslatableText("vivid.config.animation.enabled"), () -> true, () -> true, (name, defaultValue, value) -> {
			BooleanEntry entry = new BooleanEntry(name, defaultValue, value);
			entry.setSaveListener(b -> Animation.enabled = b);
			return entry;
		});

		animation.addConfigEntry(new TranslatableText("vivid.config.animation.speed"), () -> 0.2F, () -> 0.2F, (name, defaultValue, value) -> {
			FloatEntry entry = new FloatEntry(name, defaultValue, value);
			entry.setSaveListener(f -> Animation.speed = f);
			entry.setBounds(0.01F, 2F);
			return entry;
		});

		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			Random random = new Random();
			for (int i = 0; i < 3; ++i) {
				String categoryName = "vivid.category" + i + ".name";
				CategoryBuilder category = builder.startCategory(new TranslatableText(categoryName));

				for (int j = 0; j < 10; ++j) {
					category.addTooltip(new TranslatableText("vivid.category" + i + ".tooltip." + j));
				}

				for (int j = 0; j < 5; ++j) {
					String sectionName = categoryName + ".section" + j + ".name";
					SectionBuilder section = category.addSection(new TranslatableText(sectionName));

					for (int k = 0; k < 10; ++k) {
						section.addTooltip(new TranslatableText(categoryName + ".section" + j + ".tooltip." + k));
					}

					int k = 0;
					section.addConfigEntry(new TranslatableText(sectionName + ".element" + k++), () -> 0, () -> random.nextInt(10), (IntegerEntry::new))
							.addConfigEntry(new TranslatableText(sectionName + ".element" + k++), () -> 0, () -> random.nextInt(10), (IntegerEntry::new))
							.addConfigEntry(new TranslatableText(sectionName + ".element" + k++), () -> 0, () -> random.nextInt(10), (IntegerEntry::new))
							.addConfigEntry(new TranslatableText(sectionName + ".element" + k++), () -> 0, () -> random.nextInt(10), (IntegerEntry::new));

					section.addConfigEntry(new TranslatableText(sectionName + ".element" + k++), () -> 0, () -> random.nextInt(10), ((name, defaultValue, value) -> {
						IntegerEntry entry = new IntegerEntry(name, defaultValue, value);
						entry.setMin(0);

						return entry;
					}));

					section.addConfigEntry(new TranslatableText(sectionName + ".element" + k++), () -> Direction.NORTH, () -> Direction.SOUTH, EnumDropdown::new);

					section.addConfigEntry(new TranslatableText(sectionName + ".element" + k++), () -> TriState.DEFAULT, () -> TriState.TRUE, EnumSelector::new);

					section.addConfigEntry(new TranslatableText(sectionName + ".element" + k++), () -> Enabled.ON, () -> Enabled.ON, EnumSelector::new);

					section.addConfigEntry(new TranslatableText(sectionName + ".element" + k++), () -> 0, () -> random.nextInt(100), (name, defaultValue, value) -> {
						IntegerSlider entry = new IntegerSlider(name, defaultValue, value);
						entry.setBounds(0, 100);

						return entry;
					});

					section.addConfigEntry(new TranslatableText(sectionName + "element" + k++), () -> "Default", () -> "Value", StringEntry::new);

					section.addConfigEntry(new TranslatableText(sectionName + "element" + k++), () -> "xxx-xx-xxxx", () -> "012-34-5678", (name, defaultValue, value) -> {
						TextEntry<String> entry = new StringEntry(name, defaultValue, value).setMaxLength(11);
						entry.setRegex( "^(?!666|000|9\\d{2})\\d{3}-(?!00)\\d{2}-(?!0{4})\\d{4}$");

						return entry;
					});

					section.addConfigEntry(new TranslatableText(sectionName + ".element" + k++), () -> 0L, () -> (long) random.nextInt(100), (name, defaultValue, value) -> {
						LongSlider entry = new LongSlider(name, defaultValue, value);
						entry.setBounds(0L, 100L);

						return entry;
					});

					section.addConfigEntry(new TranslatableText(sectionName + ".element" + k++), () -> 0F, () -> random.nextFloat() * 100, (name, defaultValue, value) -> {
						FloatSlider entry = new FloatSlider(name, defaultValue, value);
						entry.setBounds(0F, 100F);

						return entry;
					});

					section.addConfigEntry(new TranslatableText(sectionName + ".element" + k++), () -> "Red", () -> "Green", (name, defaultValue, value) -> {
						StringDropdown entry = new StringDropdown(name, defaultValue, value);
						entry.setPossibleValues(new String[] {"Red", "Green", "Blue", "Black"});

						return entry;
					});

					section.addConfigEntry(new TranslatableText(sectionName + ".element" + k++), () -> 0D, () -> random.nextDouble() * 100D, (name, defaultValue, value) -> {
						DoubleSlider entry = new DoubleSlider(name, defaultValue, value);
						entry.setBounds(0D, 100D);

						return entry;
					});

					section.addListEntry(new TranslatableText(sectionName + ".element" + k++), name -> new ExternalButtonEntry(name, (parent) -> {}));
					section.addListEntry(new TranslatableText(sectionName + ".element" + k++), name -> new ExternalButtonEntry(name, (parent) -> {}));
					section.addListEntry(new TranslatableText(sectionName + ".element" + k), name -> new ExternalButtonEntry(name, (parent) -> {}));
				}
			}
		}
	}

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return builder::build;
	}
}
