package dev.monarkhes.vivid;

import dev.monarkhes.vivid.builders.CategoryBuilder;
import dev.monarkhes.vivid.builders.ConfigScreenBuilderImpl;
import dev.monarkhes.vivid.builders.SectionBuilder;
import dev.monarkhes.vivid.screen.ConfigScreen;
import dev.monarkhes.vivid.screen.ScreenStyle;
import dev.monarkhes.vivid.util.Alignment;
import dev.monarkhes.vivid.widgets.LabelComponent;
import dev.monarkhes.vivid.widgets.WidgetComponent;
import dev.monarkhes.vivid.widgets.containers.RowContainer;
import dev.monarkhes.vivid.widgets.value.ToggleComponent;
import dev.monarkhes.vivid.widgets.value.entry.FloatEntryWidget;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class VividConfig implements ModMenuApi {
	public static class Animation {
		public static boolean ENABLED = true;
		public static float SPEED = 0.2F;
	}

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
			WidgetComponent toggle = new ToggleComponent(parent, 0, 0, width / 2, (int) (30 * parent.getScale()), () -> true, b -> {
			}, b -> Animation.ENABLED = b, Animation.ENABLED);
			return new RowContainer(parent, x, y, index, true, label, toggle);
		});

		animation.add((parent, width, x, y, index) -> {
			WidgetComponent label = new LabelComponent(parent, 0, 0, width / 2, (int) (30 * parent.getScale()), new TranslatableText("vivid.config.animation.speed"), true);
			WidgetComponent entry = new FloatEntryWidget(parent, x, y, width / 2, (int) (30 * parent.getScale()), Alignment.RIGHT, () -> 0.2F, f -> {
			}, f -> Animation.SPEED = f, Animation.SPEED);
			return new RowContainer(parent, x, y, index, true, label, entry);
		});
	}

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory () {
		return parent -> new ConfigScreen(parent, builder);
	}
}