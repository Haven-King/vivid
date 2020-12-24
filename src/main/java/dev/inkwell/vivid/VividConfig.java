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
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Random;

public class VividConfig implements ModMenuApi {
	public static class Animation {
		public static boolean ENABLED = true;
		public static float SPEED = 0.2F;
	}

	public static final ScreenStyle STYLE = new ScreenStyle() {{
		this.backgroundTexture = new Identifier("textures/block/cobblestone.png");
		this.backgroundColor = 0xBB404040;
	}};

	private final ConfigScreenBuilder builder;

	public VividConfig() {
		builder = new ConfigScreenBuilder().withStyle(STYLE);

		CategoryBuilder config = builder.startCategory(new TranslatableText("vivid.config"));

		SectionBuilder animation = config.addSection(new TranslatableText("vivid.config.animation"));
		animation.addConfigEntry(new TranslatableText("vivid.config.animation.enabled"), () -> true, () -> Animation.ENABLED, b -> Animation.ENABLED = (boolean) b, (name, defaultValue, consumer, value) -> {
			BooleanEntry entry = new BooleanEntry(name, defaultValue, consumer, value);
			entry.setSaveListener(consumer);
			return entry;
		});

		animation.addConfigEntry(new TranslatableText("vivid.config.animation.speed"), () -> 0.2F, () -> Animation.SPEED, f -> Animation.SPEED = (float) f, (name, defaultValue, consumer, value) -> {
			FloatEntry entry = new FloatEntry(name, defaultValue, consumer, value);
			entry.setSaveListener(consumer);
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
					section.addConfigEntry(new TranslatableText(sectionName + ".element" + k++), () -> 0, () -> random.nextInt(10), t -> {}, (IntegerEntry::new))
							.addConfigEntry(new TranslatableText(sectionName + ".element" + k++), () -> 0, () -> random.nextInt(10), t -> {}, (IntegerEntry::new))
							.addConfigEntry(new TranslatableText(sectionName + ".element" + k++), () -> 0, () -> random.nextInt(10), t -> {}, (IntegerEntry::new))
							.addConfigEntry(new TranslatableText(sectionName + ".element" + k++), () -> 0, () -> random.nextInt(10), t -> {}, (IntegerEntry::new));

					section.addConfigEntry(new TranslatableText(sectionName + ".element" + k++), () -> 0, () -> random.nextInt(10), t -> {}, (name, defaultValue, consumer, value) -> {
						IntegerEntry entry = new IntegerEntry(name, defaultValue, consumer, value);
						entry.setMin(0);

						return entry;
					});

					section.addConfigEntry(new TranslatableText(sectionName + ".element" + k++), () -> Direction.NORTH, () -> Direction.SOUTH, t -> {}, EnumDropdown::new);

					section.addConfigEntry(new TranslatableText(sectionName + ".element" + k++), () -> TriState.DEFAULT, () -> TriState.TRUE, t -> {}, EnumSelector::new);

					section.addConfigEntry(new TranslatableText(sectionName + ".element" + k++), () -> Enabled.ON, () -> Enabled.ON,  t -> {}, EnumSelector::new);

					section.addConfigEntry(new TranslatableText(sectionName + ".element" + k++), () -> 0, () -> random.nextInt(100), t -> {}, (name, defaultValue, consumer, value) -> {
						IntegerSlider entry = new IntegerSlider(name, defaultValue, consumer, value);
						entry.setBounds(0, 100);

						return entry;
					});

					section.addConfigEntry(new TranslatableText(sectionName + "element" + k++), () -> "Default", () -> "Value", t -> {}, StringEntry::new);

					section.addConfigEntry(new TranslatableText(sectionName + "element" + k++), () -> "xxx-xx-xxxx", () -> "012-34-5678", t -> {}, (name, defaultValue, saveConsumer, value) -> {
						TextEntry<String> entry = new StringEntry(name, defaultValue, saveConsumer, value).setMaxLength(11);
						entry.setRegex( "^(?!666|000|9\\d{2})\\d{3}-(?!00)\\d{2}-(?!0{4})\\d{4}$");

						return entry;
					});

					section.addConfigEntry(new TranslatableText(sectionName + ".element" + k++), () -> 0L, () -> (long) random.nextInt(100), t -> {}, (name, defaultValue, saveConsumer, value) -> {
						LongSlider entry = new LongSlider(name, defaultValue, saveConsumer, value);
						entry.setBounds(0L, 100L);

						return entry;
					});

					section.addConfigEntry(new TranslatableText(sectionName + ".element" + k++), () -> 0F, () -> random.nextFloat() * 100, t -> {}, (name, defaultValue, saveConsumer, value) -> {
						FloatSlider entry = new FloatSlider(name, defaultValue, saveConsumer, value);
						entry.setBounds(0F, 100F);

						return entry;
					});

					section.addConfigEntry(new TranslatableText(sectionName + ".element" + k++), () -> "Red", () -> "Green", t -> {}, (name, defaultValue, saveConsumer, value) -> {
						StringDropdown entry = new StringDropdown(name, defaultValue, saveConsumer, value);
						entry.setPossibleValues(new String[] {"Red", "Green", "Blue", "Black"});

						return entry;
					});

					section.addConfigEntry(new TranslatableText(sectionName + ".element" + k++), () -> 0D, () -> random.nextDouble() * 100D, t -> {}, (name, defaultValue, saveConsumer, value) -> {
						DoubleSlider entry = new DoubleSlider(name, defaultValue, saveConsumer, value);
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
