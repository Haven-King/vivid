package dev.inkwell.vivid.screen;

import dev.inkwell.vivid.DrawableExtensions;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;

import static dev.inkwell.vivid.Vivid.BLUR;

public class ScreenStyle extends DrawableHelper implements DrawableExtensions {
	public static final ScreenStyle DEFAULT = new ScreenStyle();
	public static final ScreenStyle NETHER;

	static {
		NETHER = new ScreenStyle() {
			private final RotatingCubeMapRenderer backgroundRenderer =
					new RotatingCubeMapRenderer(TitleScreen.PANORAMA_CUBE_MAP);

			@Override
			protected void renderBackground(ConfigScreen screen, Screen parent, MatrixStack matrices, float tickDelta) {
				fill(matrices, 0, 0, 1000, 1000, 0xFFFFFFFF, 1F);
				this.backgroundRenderer.render(tickDelta, 1F);
			}
		};

		NETHER.blurAmount = 8F;
	}

	public ElementStyle<Style> categoryColor;
	public Style sectionColor;
	public ElementStyle<Style> labelColor;
	public ElementStyle<Integer> scrollbarColor = new ElementStyle<>(
			0xBB888888,
			0xBBAAAAAA,
			0xBBCCCCCC);

	public int accentColor;
	public int gradientColor;
	public float blurAmount = 0F;

	public ScreenStyle() {
		this.categoryColor = new ElementStyle<>(
				Style.EMPTY.withColor(TextColor.fromRgb(0xFFFFFFFF)).withBold(true).withUnderline(true),
				Style.EMPTY.withColor(TextColor.fromRgb(0xFFAAAAFF)).withBold(true),
				Style.EMPTY.withColor(TextColor.fromRgb(0xFFAAAAAA))
		);

		this.sectionColor = Style.EMPTY.withColor(TextColor.fromRgb(0xFFFFFFFF));
		this.labelColor = new ElementStyle<>(Style.EMPTY.withColor(TextColor.fromRgb(0xFFFFFFFF)));
		this.accentColor = 0xFFFFFFFF;
		this.gradientColor = 0x00000000;
	}

	public void renderScrollbar(MatrixStack matrices, int x, int y, int width, int height, boolean active, boolean hovered) {
		fill(matrices, x, y, x + width, y + height, scrollbarColor.color(active, hovered), 1F);
	}

	public void renderCategoryButtonDecorations(CategoryButtonWidget button, MatrixStack matrices, int x, int y, int width, int height) {

	}

	public final void renderBackgroundFromPresets(ConfigScreen screen, Screen parent, MatrixStack matrices, float tickDelta) {
		this.renderBackground(screen, parent, matrices, tickDelta);

		if (blurAmount > 0F) {
			BLUR.setUniformValue("Start", 0F, 0F);
			BLUR.setUniformValue("End", 1F, 1F);
			BLUR.setUniformValue("Progress", 1F);
			BLUR.setUniformValue("Radius", blurAmount);
			BLUR.render(1F);
		}
	}

	protected void renderBackground(ConfigScreen screen, Screen parent, MatrixStack matrices, float tickDelta) {
		if (parent == null) {
			screen.renderBackground(matrices);
		} else {
			parent.renderBackground(matrices);
		}
	}

	public void renderDecorations(MatrixStack matrices, int mouseX, int mouseY, float delta, int width, int height) {
		fillGradient(matrices, 0, 0, width, height / 8, 0x88000000 | (gradientColor & 0x00FFFFFF), (gradientColor & 0x00FFFFFF));
		fillGradient(matrices, 0, height - height / 8, width, height, (gradientColor & 0x00FFFFFF), 0x88000000 | (gradientColor & 0x00FFFFFF));

		line(matrices, 0, width, 12.125F, 12.125F, 0x88000000 | (accentColor & 0x00FFFFFF));
	}

	public interface FromOrdinal<T> {
		T fromOrdinal(int offset);
	}

	public static class ElementStyle<T> {
		public T active;
		public T hovered;
		public T base;

		public ElementStyle(T base) {
			this(base, base, base);
		}

		public ElementStyle(T active, T hovered, T base) {
			this.active = active;
			this.hovered = hovered;
			this.base = base;
		}

		public T color(boolean active, boolean hovered) {
			return active ? this.active : hovered ? this.hovered : this.base;
		}
	}
}
