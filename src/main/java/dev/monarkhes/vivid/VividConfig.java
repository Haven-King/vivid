package dev.monarkhes.vivid;

import dev.monarkhes.vivid.builders.CategoryBuilder;
import dev.monarkhes.vivid.builders.ConfigScreenBuilderImpl;
import dev.monarkhes.vivid.builders.SectionBuilder;
import dev.monarkhes.vivid.builders.WidgetComponentBuilder;
import dev.monarkhes.vivid.screen.ConfigScreen;
import dev.monarkhes.vivid.screen.ScreenStyle;
import dev.monarkhes.vivid.util.Alignment;
import dev.monarkhes.vivid.util.Array;
import dev.monarkhes.vivid.util.Enabled;
import dev.monarkhes.vivid.util.Table;
import dev.monarkhes.vivid.widgets.LabelComponent;
import dev.monarkhes.vivid.widgets.TextButton;
import dev.monarkhes.vivid.widgets.WidgetComponent;
import dev.monarkhes.vivid.widgets.compound.ArrayWidget;
import dev.monarkhes.vivid.widgets.compound.TableWidget;
import dev.monarkhes.vivid.widgets.containers.RowContainer;
import dev.monarkhes.vivid.widgets.value.EnumDropdownWidget;
import dev.monarkhes.vivid.widgets.value.EnumSelectorComponent;
import dev.monarkhes.vivid.widgets.value.ToggleComponent;
import dev.monarkhes.vivid.widgets.value.entry.FloatEntryWidget;
import dev.monarkhes.vivid.widgets.value.entry.IntegerEntryWidget;
import dev.monarkhes.vivid.widgets.value.entry.StringEntryWidget;
import dev.monarkhes.vivid.widgets.value.slider.IntegerSliderWidget;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import net.fabricmc.fabric.api.util.TriState;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.Random;

public class VividConfig implements ModMenuApi {
	public static class Animation {
		public static boolean ENABLED = true;
		public static float SPEED = 0.2F;
	}

	private static final WidgetComponentBuilder<Integer> INT_SLIDER_BUILDER =
			(parent, x, y, width, height, defaultValueSupplier, changedListener, saveConsumer, value) ->
					new IntegerSliderWidget(parent, x, y, width, height, defaultValueSupplier, changedListener, saveConsumer, value, -10, 10);

	public static final Array<Integer> TEST1 = new Array<>(Integer.class, () -> 5, 1, 2, 3, 4, 5, 6, 7);
	public static final Table<Integer> TEST2 = new Table<>(Integer.class, () -> 5,
			new Table.Entry<>("alpha", 7),
			new Table.Entry<>("beta", 6),
			new Table.Entry<>("gamma", 5)
	);

	public static final ScreenStyle STYLE = new ScreenStyle() {
		{
			this.backgroundTexture = new Identifier("textures/block/cobblestone.png");
			this.backgroundColor = 0x80808080;
		}

		@Override
		public void renderDecorations(MatrixStack matrices, int mouseX, int mouseY, float delta, int screenWidth, int screenHeight, int headerHeight) {
			super.renderDecorations(matrices, mouseX, mouseY, delta, screenWidth, screenHeight, headerHeight);
			fillGradient(matrices, 0, 0, screenWidth, screenHeight, 0x88000000 | (gradientColor & 0x00FFFFFF), (gradientColor & 0x00FFFFFF));
		}
	};

	private final ConfigScreenBuilderImpl builder;

	public VividConfig() {
		builder = new ConfigScreenBuilderImpl().withStyle(STYLE);

		CategoryBuilder config = builder.startCategory(new TranslatableText("vivid.config"));

		SectionBuilder animation = config.addSection(new TranslatableText("vivid.config.animation"));
		animation.add((parent, width, x, y, index) -> {
			WidgetComponent label = new LabelComponent(parent, 0, 0, width / 2, (int) (30 * parent.getScale()), new TranslatableText("vivid.config.animation.enabled"), true);
			WidgetComponent toggle = new ToggleComponent(parent, 0, 0, width / 2, (int) (30 * parent.getScale()), () -> true, b -> {}, b -> Animation.ENABLED = b, Animation.ENABLED);
			return new RowContainer(parent, x, y, index, true, label, toggle);
		});

//		animation.addConfigEntry(new TranslatableText(
//				"vivid.config.animation.enabled"), () -> true, () -> Animation.ENABLED, b -> Animation.ENABLED = (boolean) b, (name, defaultValue, consumer, value, type) -> {
//			BooleanEntry entry = new BooleanEntry(name, defaultValue, consumer, value, type);
//			entry.setSaveListener(consumer);
//			return entry;
//		});

//		animation.addConfigEntry(new TranslatableText("vivid.config.animation.speed"), () -> 0.2F, () -> Animation.SPEED, f -> Animation.SPEED = (float) f, (name, defaultValue, consumer, value, type) -> {
//			FloatEntry entry = new FloatEntry(name, defaultValue, consumer, value, type);
//			entry.setSaveListener(consumer);
//			entry.setBounds(0.01F, 2F);
//			return entry;
//		});

		animation.add((parent, width, x, y, index) -> {
			WidgetComponent label = new LabelComponent(parent, 0, 0, width / 2, (int) (30 * parent.getScale()), new TranslatableText("vivid.config.animation.speed"), true);
			WidgetComponent entry = new FloatEntryWidget(parent, x, y, width / 2, (int) (30 * parent.getScale()), Alignment.RIGHT, () -> 0.2F, f -> {}, f -> Animation.SPEED = f, Animation.SPEED);
			return new RowContainer(parent, x, y, index, true, label, entry);
		});

		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			Random random = new Random();

			{
//				CategoryBuilder categoryBuilder = builder.startCategory(new LiteralText("Test"));
//				SectionBuilder section = categoryBuilder.addSection(LiteralText.EMPTY.copy());
//
//				section.add((parent, width, x, y, index) -> {
//					WidgetComponent label = new LabelComponent(parent, 0, 0, width / 2, (int) (30 * parent.getScale()), new Trans)
//				});

//				new LiteralText("List Test"),
//						() -> new Array<>(Integer.class, () -> 0, IntegerEntry::new, 0, 1, 2, 3),
//						() -> new Array<>(TEST),
//						array -> {
//							//noinspection unchecked
//							TEST.copy((Array<Integer>) array);
//						},
//						ArrayEntry::new

				animation.add((parent, width, x, y, index) -> {
					WidgetComponent label = new LabelComponent(parent, 0, 0, width / 2, (int) (30 * parent.getScale()), new TranslatableText("vivid'config.animation.list_test"), true);
					WidgetComponent entry = new ArrayWidget<>(
							parent,
							x,
							y,
							width / 2,
							(int) (30 * parent.getScale()),
							() -> new Array<>(Integer.class, () -> 0, 0, 1, 2, 3),
							a -> {},
							TEST1::copy,
							TEST1,
							new LiteralText("List Test"),
							INT_SLIDER_BUILDER
					);

					return new RowContainer(parent, x, y, index, true, label, entry);
				});

				animation.add((parent, width, x, y, index) -> {
					WidgetComponent label = new LabelComponent(parent, 0, 0, width / 2, (int) (30 * parent.getScale()), new TranslatableText("vivid.config.animation.table_test1"), true);
					WidgetComponent entry = new TableWidget<>(
							parent,
							x,
							y,
							width / 2,
							(int) (30 * parent.getScale()),
							() -> new Table<>(Integer.class, () -> 0,
									new Table.Entry<>("alpha", 1),
									new Table.Entry<>("beta", 2),
									new Table.Entry<>("gamma", 3)
							),
							a -> {
							},
							TEST2::copy,
							TEST2,
							new LiteralText("Table Test 1"),
							INT_SLIDER_BUILDER);

					return new RowContainer(parent, x, y, index, true, label, entry);
				});
			}

			animation.add((parent, width, x, y, index) -> {
				WidgetComponent label = new LabelComponent(parent, 0, 0, width / 2, (int) (30 * parent.getScale()), new TranslatableText("vivid.config.animation.table_test2"), true);
				WidgetComponent entry = new TableWidget<>(
						parent,
						x,
						y,
						width / 2,
						(int) (30 * parent.getScale()),
						() -> new Table<>(Integer.class, () -> 0,
								new Table.Entry<>("alpha", 1),
								new Table.Entry<>("beta", 2),
								new Table.Entry<>("gamma", 3)
						),
						a -> {},
						TEST2::copy,
						TEST2,
						new LiteralText("Table Test 2"),
						INT_SLIDER_BUILDER, false
				);

				return new RowContainer(parent, x, y, index, true, label, entry);
			});

			for (int i = 0; i < 2; ++i) {
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

					final MutableInt k = new MutableInt();

					for (int l = 0; l < 4; ++l) {
						section.add((parent, width, x, y, index) -> {
							WidgetComponent label = new LabelComponent(parent, 0, 0, width / 2, (int) (30 * parent.getScale()), new TranslatableText(sectionName + ".element" + k.getAndIncrement()), true);
							WidgetComponent value = new IntegerEntryWidget(parent, 0, 0, width / 2, (int) (30 * parent.getScale()), Alignment.RIGHT, () -> 0, t -> {}, t -> {}, random.nextInt(10));
							return new RowContainer(parent, x, y, index, true, label, value);
						});
					}

					section.add((parent, width, x, y, index) -> {
						WidgetComponent label = new LabelComponent(parent, 0, 0, width / 2, (int) (30 * parent.getScale()), new TranslatableText(sectionName + ".element" + k.getAndIncrement()), true);
						WidgetComponent value = new EnumDropdownWidget<>(parent, 0, 0, width / 2, (int) (30 * parent.getScale()), () -> Direction.NORTH, e -> {}, e -> {}, Direction.random(random));
						return new RowContainer(parent, x, y, index, true, label, value);
					});

					section.add((parent, width, x, y, index) -> {
						WidgetComponent label = new LabelComponent(parent, 0, 0, width / 2, (int) (30 * parent.getScale()), new TranslatableText(sectionName + ".element" + k.getAndIncrement()), true);
						WidgetComponent value = new EnumSelectorComponent<>(parent, 0, 0, width / 2, (int) (30 * parent.getScale()), () -> TriState.DEFAULT, t -> {}, t -> {}, TriState.TRUE);
						return new RowContainer(parent, x, y, index, true, label, value);
					});

					section.add((parent, width, x, y, index) -> {
						WidgetComponent label = new LabelComponent(parent, 0, 0, width / 2, (int) (30 * parent.getScale()), new TranslatableText(sectionName + ".element" + k.getAndIncrement()), true);
						WidgetComponent value = new EnumSelectorComponent<>(parent, 0, 0, width / 2, (int) (30 * parent.getScale()), () -> Enabled.ON, t -> {}, t -> {}, Enabled.OFF);
						return new RowContainer(parent, x, y, index, true, label, value);
					});

					section.add((parent, width, x, y, index) -> {
						WidgetComponent label = new LabelComponent(parent, 0, 0, width / 2, (int) (30 * parent.getScale()), new TranslatableText(sectionName + ".element" + k.getAndIncrement()), true);
						WidgetComponent value = new IntegerSliderWidget(parent, 0, 0, width / 2, (int) (30 * parent.getScale()), () -> random.nextInt(100), t -> {}, t -> {}, 50, 0, 100);
						return new RowContainer(parent, x, y, index, true, label, value);
					});

					section.add((parent, width, x, y, index) -> {
						WidgetComponent label = new LabelComponent(parent, 0, 0, width / 2, (int) (30 * parent.getScale()), new TranslatableText(sectionName + ".element" + k.getAndIncrement()), true);
						StringEntryWidget value = new StringEntryWidget(parent, 0, 0, width / 2, (int) (30 * parent.getScale()), Alignment.RIGHT, () -> "xxx-xx-xxxx", t -> {}, t -> {}, "012-34-5678");
						value.setRegex( "^(?!666|000|9\\d{2})\\d{3}-(?!00)\\d{2}-(?!0{4})\\d{4}$");
						return new RowContainer(parent, x, y, index, true, label, value);
					});

					section.add((parent, width, x, y, index) -> {
						WidgetComponent label = new LabelComponent(parent, 0, 0, width / 2, (int) (30 * parent.getScale()), new TranslatableText(sectionName + ".element" + k.getAndIncrement()), true);
						WidgetComponent value = new StringEntryWidget(parent, 0, 0, width / 2, (int) (30 * parent.getScale()), Alignment.RIGHT, () -> "default", t -> {}, t -> {}, "value");
						return new RowContainer(parent, x, y, index, true, label, value);
					});

					section.add((parent, width, x, y, index) -> {
						WidgetComponent label = new LabelComponent(parent, 0, 0, width / 2, (int) (30 * parent.getScale()), new TranslatableText(sectionName + ".element" + k.getAndIncrement()), true);
						WidgetComponent value = new TextButton(parent, 0, 0, width / 2,  (int) (30 * parent.getScale()), 0, new LiteralText("▶"), Alignment.RIGHT, button -> true);
						return new RowContainer(parent, x, y, index, true, label, value);
					});
//
//					section.addConfigEntry(new TranslatableText(sectionName + ".element" + k++), () -> 0L, () -> (long) random.nextInt(100), t -> {}, (name, defaultValue, saveConsumer, value, type) -> {
//						LongSlider entry = new LongSlider(name, defaultValue, saveConsumer, value, type);
//						entry.setBounds(0L, 100L);
//
//						return entry;
//					});
//
//					section.addConfigEntry(new TranslatableText(sectionName + ".element" + k++), () -> 0F, () -> random.nextFloat() * 100, t -> {}, (name, defaultValue, saveConsumer, value, type) -> {
//						FloatSlider entry = new FloatSlider(name, defaultValue, saveConsumer, value, type);
//						entry.setBounds(0F, 100F);
//
//						return entry;
//					});
//
//					section.addConfigEntry(new TranslatableText(sectionName + ".element" + k++), () -> "Red", () -> "Green", t -> {}, (name, defaultValue, saveConsumer, value, type) -> {
//						StringDropdown entry = new StringDropdown(name, defaultValue, saveConsumer, value, type);
//						entry.setPossibleValues(new String[] {"Red", "Green", "Blue", "Black"});
//
//						return entry;
//					});
//
//					section.addConfigEntry(new TranslatableText(sectionName + ".element" + k++), () -> 0D, () -> random.nextDouble() * 100D, t -> {}, (name, defaultValue, saveConsumer, value, type) -> {
//						DoubleSlider entry = new DoubleSlider(name, defaultValue, saveConsumer, value, type);
//						entry.setBounds(0D, 100D);
//
//						return entry;
//					});
//
//					section.addListEntry(new TranslatableText(sectionName + ".element" + k++), name -> new ExternalButtonEntry(name, (parent) -> {}));
//					section.addListEntry(new TranslatableText(sectionName + ".element" + k++), name -> new ExternalButtonEntry(name, (parent) -> {}));
//					section.addListEntry(new TranslatableText(sectionName + ".element" + k), name -> new ExternalButtonEntry(name, (parent) -> {}));
				}
			}
		}
	}

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> new ConfigScreen(parent, builder);
	}
}
