package dev.inkwell.vivid.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.inkwell.vivid.DrawableExtensions;
import dev.inkwell.vivid.constraints.Constraint;
import dev.inkwell.vivid.entry.base.ListEntry;
import dev.inkwell.vivid.entry.base.ValueEntry;
import dev.inkwell.vivid.util.Group;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TickableElement;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.*;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

import static dev.inkwell.vivid.Vivid.BLUR;

// TODO: This class needs to be cleaned up/split up in general
public class ConfigScreen extends Screen implements DrawableExtensions {
	private final Screen parent;
	private final List<Group<Group<ListEntry>>> categories;
	private final int categoryWidth;

	private boolean isSaveDialogOpen = false;

	private ScreenStyle style = ScreenStyle.DEFAULT;

	private int activeCategory = 0;
	private int scrollAmount = 0;

	private int contentWidth;
	private int headerSize;
	private int visibleHeight;
	private float margin;
	private float scale;

	private int lastMouseX;
	private int lastMouseY;

	private double clickedX;
	private float lastTickDelta;

	private int lastY = 0;

	private List<Text> tooltips = new ArrayList<>();
	private ListEntry hovered = null;

	// TODO: Better error hoisting?
	@SuppressWarnings({"FieldCanBeLocal", "unused"})
	private boolean hasError = false;

	private AbstractButtonWidget yesButton;
	private AbstractButtonWidget noButton;

	public ConfigScreen(Screen parent, List<Group<Group<ListEntry>>> categories) {
		super(categories.get(0).getName());
		this.parent = parent;
		this.categories = categories;

		int l = 0;

		TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		for (Group<?> group : this.categories) {
			l = Math.max(l, textRenderer.getWidth(group.getName()));
		}

		this.categoryWidth = l;
	}

	public ConfigScreen withStyle(ScreenStyle style) {
		this.style = style;
		return this;
	}

	@Override
	public void init(MinecraftClient client, int width, int height) {
		super.init(client, width, height);

		headerSize = client.textRenderer.fontHeight * 3;
		contentWidth = height > width ? (width - 12) : width / 2;
		visibleHeight = this.height - headerSize;
		margin = height > width ? 6 : width / 4F;

		double test = client.getWindow().getScaleFactor();

		scale = (float) (2F / test);

		int padding = 3;
		int buttonWidth = 45;
		int buttonHeight = 15;

		yesButton = new FancyButton(this,
				width / 2 - buttonWidth - padding,
				height / 2,
				buttonWidth,
				buttonHeight,
				new TranslatableText("gui.yes"),
				button -> {
					for (Group<ListEntry> section : categories.get(activeCategory)) {
						for (ListEntry entry : section) {
							if (entry instanceof ValueEntry && ((ValueEntry<?>) entry).hasChanged()) {
								((ValueEntry<?>) entry).save();
							}
						}
					}

					this.onClose();
				}
		);

		noButton = new FancyButton(this,
				width / 2 + padding,
				height / 2,
				buttonWidth,
				buttonHeight,
				new TranslatableText("gui.no"),
				button -> {
					this.onClose();
				}
		);

		yesButton.visible = false;
		noButton.visible = false;

		for (int i = 0; i < categories.size(); ++i) {
			int categoryId = i;
			ButtonWidget button = new CategoryButtonWidget(
					this,
					(int) (margin + i * (contentWidth / categories.size()) + (contentWidth / (categories.size() * 2)) - categoryWidth / 2),
					0,
					categoryWidth,
					12,
					categories.get(i).getName(),
					(b) -> this.setActiveCategory(categoryId), categories.get(i).getTooltips());

			if (i == activeCategory) {
				button.active = false;
			}

			for (Group<ListEntry> section : categories.get(i)) {
				for (ListEntry entry : section) {
					entry.init(this);
				}
			}

			this.addButton(button);
		}
	}

	@Override
	public void renderBackground(MatrixStack matrices) {
		this.style.renderBackgroundFromPresets(this, this.parent, matrices, lastTickDelta);
	}

	@Override
	public void resize(MinecraftClient client, int width, int height) {
		super.resize(client, width, height);

		if (this.parent != null) {
			parent.resize(client, width, height);
		}
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float tickDelta) {
		if (this.client == null) return;

		int useMouseX;
		int useMouseY;

		if (this.isSaveDialogOpen) {
			useMouseX = lastMouseX;
			useMouseY = lastMouseY;
		} else {
			useMouseX = mouseX;
			useMouseY = mouseY;
		}

		this.lastTickDelta = tickDelta;
		this.renderBackground(matrices);
		this.style.renderDecorations(matrices, useMouseX, useMouseY, tickDelta, this.width, this.height, this.headerSize);

		int contentHeight = lastY - headerSize;

		Group<Group<ListEntry>> sections = categories.get(activeCategory);
		int sectionColor = this.style.sectionColor.getColor() != null
				? this.style.sectionColor.getColor().getRgb()
				: 0xFFFFFFFF;

		if (contentHeight > visibleHeight) {
			float ratio = visibleHeight / (float) contentHeight;
			int startX = height > width ? (int) (contentWidth + margin + 2) : (int) (margin * 3 + 2);
			int startY = (int) (this.headerSize - scrollAmount * ratio) + (sections.get(0).getName().getString().isEmpty() ? 0 : 10);
			int height = (int) (ratio * visibleHeight) - this.headerSize;
			boolean hovered = useMouseX >= startX && useMouseY >= startY && useMouseX <= startX + 3 && useMouseY <= startY + height;
			this.style.renderScrollbar(matrices, startX, startY, 3, height, false, hovered);
		}

		super.render(matrices, useMouseX, useMouseY, tickDelta);


		matrices.push();
		matrices.translate(margin, 0, 0F);

		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		Window window = client.getWindow();
		float test = ((this.height - this.headerSize) / (float) this.height);
		GL11.glScissor(0, 0, window.getFramebufferWidth(), (int) (window.getFramebufferHeight() * test));

		int y = this.headerSize;

		this.hasError = false;
		this.hovered = null;

		int focusedJ = 0;
		int focusedY = 0;

		for (int i = 0; i < sections.size(); ++i) {
			Group<ListEntry> section = sections.get(i);

			if (!section.getName().getString().isEmpty()) {
				this.draw(matrices, this.textRenderer, section.getName(), 0, y + scrollAmount, sectionColor, scale);

				if (useMouseX >= margin && useMouseX <= margin + this.contentWidth / 2F && useMouseY >= y + scrollAmount && useMouseY <= y + scrollAmount + 10) {
					this.addTooltips(section.getTooltips());
				}

				y += 10;
			}

			for (int j = 0; j < section.size(); ++j) {
				ListEntry entry = section.get(j);

				if (this.getFocused() instanceof ListEntry && entry == this.getFocused()) {
					focusedJ = j;
					focusedY = y + scrollAmount;
				} else {
					entry.render(matrices, j, contentWidth, y + scrollAmount, (int) (useMouseX - margin), useMouseY, tickDelta);
				}

				if (useMouseY > this.headerSize && entry.isMouseOver(useMouseX - margin, useMouseY) && (entry == this.getFocused() || this.getFocused() == null)) {
					entry.addTooltipsToList(tooltips);
					this.hovered = entry;
				}

				if (entry instanceof Constraint) {
					this.hasError |= !((Constraint) entry).passes();
				}

				y += entry.getHeight();
			}

			y += 15;
		}

		GL11.glDisable(GL11.GL_SCISSOR_TEST);

		if (this.getFocused() instanceof ListEntry) {
			((ListEntry) this.getFocused()).render(matrices, focusedJ, contentWidth, focusedY, (int) (useMouseX - margin), useMouseY, tickDelta);
		}

		matrices.pop();

		lastY = y - this.headerSize;

		if (!tooltips.isEmpty()) {
			this.renderTooltip(matrices, tooltips, useMouseX, useMouseY);
			tooltips.clear();
		}

		if (this.isSaveDialogOpen) {
			fill(matrices, 0, 0, width, height, 0x80000000);
			BLUR.setUniformValue("Progress", 1F);
			BLUR.setUniformValue("Radius", 4F);
			BLUR.setUniformValue("Start", 0F, 0F);
			BLUR.setUniformValue("End", 1F, 1F);
			BLUR.render(1F);

			int padding = 3;
			int buttonWidth = 45;
			int buttonHeight = 20;

			int changedCount = this.changedCount();
			drawCenteredText(matrices, textRenderer, new TranslatableText("vivid.unsaved.count." + (changedCount > 1 ? "plural" : "singular"), changedCount), width / 2F, height / 2F - textRenderer.fontHeight / 2F - buttonHeight, 0xFFFFFFFF, 1.25F * scale);
			drawCenteredText(matrices, textRenderer, new TranslatableText("vivid.unsaved.prompt"), width / 2F, height / 2F - (buttonHeight / 4F) * 3, 0xFFFFFFFF, 1.25F * scale);

			yesButton.render(matrices, mouseX, mouseY, tickDelta);
			noButton.render(matrices, mouseX, mouseY, tickDelta);
		} else {
			lastMouseX = mouseX;
			lastMouseY = mouseY;
		}
	}

	private void setActiveCategory(int category) {
		this.activeCategory = category;

		for (int i = 0; i < categories.size(); ++i) {
			this.buttons.get(i).active = i != category;
		}

		this.scrollAmount = 0;
	}

	@Override
	public void onClose() {
		if (this.client == null) return;

		this.client.openScreen(this.parent);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		amount *= 5;
		int scrollAmount = (int) Math.min(Math.max(minScrollAmount(), this.scrollAmount + amount), maxScrollAmount());
		boolean changed = scrollAmount != this.scrollAmount;
		this.scrollAmount = scrollAmount;

		return changed;
	}

	private int maxScrollAmount() {
		return 0;
	}

	private int minScrollAmount() {
		return -(this.lastY - this.height + this.headerSize);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if (this.isSaveDialogOpen) {
			return false;
		} else {
			boolean bl = height > width
					? clickedX > margin + contentWidth + 3
					: clickedX > (margin) * 3 + 2 && clickedX < margin * 3 + 5;
			if (bl) {
				return mouseScrolled(mouseX, mouseY, -deltaY);
			} else {
				for (Group<ListEntry> section : categories.get(activeCategory)) {
					for (ListEntry entry : section) {
						if (entry.mouseDragged(mouseX - margin, mouseY, button, deltaX, deltaY)) {
							return true;
						}
					}
				}
			}

			return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (this.isSaveDialogOpen) {
			if (yesButton.mouseClicked(mouseX, mouseY, button)) {
				return true;
			} else if (noButton.mouseClicked(mouseX, mouseY, button)) {
				this.isSaveDialogOpen = false;
				yesButton.visible = false;
				noButton.visible = false;
				return true;
			}

			return false;
		} else {
			boolean bl = super.mouseClicked(mouseX, mouseY, button);
			this.setFocused(null);

			clickedX = mouseX;

			if (bl) {
				for (Group<ListEntry> section : categories.get(activeCategory)) {
					for (ListEntry entry : section) {
						entry.setFocused(false);
					}
				}
			} else {
				if (getFocused() == null || !getFocused().isMouseOver(mouseX - margin, mouseY)) {
					for (Group<ListEntry> section : categories.get(activeCategory)) {
						for (ListEntry entry : section) {
							if (entry.holdsFocus()) {
								entry.setFocused(entry.isMouseOver(mouseX - margin, mouseY));

								if (entry.isFocused()) {
									this.setFocused(entry);
								}
							}

							bl = bl || entry.mouseClicked(mouseX - margin, mouseY, button);
						}
					}
				} else {
					bl = getFocused().mouseClicked(mouseX - margin, mouseY, button);
				}
			}

			if (!bl) {
				this.setFocused(null);
			}

			return bl;
		}
	}

	@Override
	public boolean charTyped(char chr, int keyCode) {
		if (this.isSaveDialogOpen) {
			return false;
		} else {
			if (this.getFocused() != null) {
				return this.getFocused().charTyped(chr, keyCode);
			}

			return false;
		}
	}

	@Override
	public void tick() {
		if (this.isSaveDialogOpen) {

		} else {
			if (this.getFocused() != null && this.getFocused() instanceof TickableElement) {
				((TickableElement) this.getFocused()).tick();
			}
		}
	}

	private int changedCount() {
		int changed = 0;

		for (Group<Group<ListEntry>> category : this.categories) {
			for (Group<ListEntry> section : category) {
				for (ListEntry entry : section) {
					if (entry instanceof ValueEntry && ((ValueEntry<?>) entry).hasChanged()) {
						++changed;
					}
				}
			}
		}

		return changed;
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (this.isSaveDialogOpen) {
			if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
				this.isSaveDialogOpen = false;
			}

			return false;
		} else {
			if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
				int changed = this.changedCount();

				if (changed > 0) {
					this.isSaveDialogOpen = true;
					yesButton.visible = true;
					noButton.visible = true;
					return false;
				}
			}

			boolean bl = false;

			if (this.getFocused() != null) {
				bl = this.getFocused().keyPressed(keyCode, scanCode, modifiers);
			}

			return bl || super.keyPressed(keyCode, scanCode, modifiers);
		}
	}

	public ScreenStyle getStyle() {
		return this.style;
	}

	public void addTooltips(List<Text> tooltips) {
		this.tooltips.addAll(tooltips);
	}

	@Override
	public void renderOrderedTooltip(MatrixStack matrices, List<? extends OrderedText> lines, int x, int y) {
		if (!lines.isEmpty() && this.client != null) {
			int i = 0;

			for (OrderedText orderedText : lines) {
				int j = this.textRenderer.getWidth(orderedText);
				if (j > i) {
					i = j;
				}
			}

			float k = x - (i / 4F);
			float l = y - 10 - (textRenderer.fontHeight * lines.size() * scale);
			int n = 8;
			if (lines.size() > 1) {
				n += 2 + (lines.size() - 1) * 10;
			}

			k *= scale;
			l *= scale;

			if (l + n + 6 > this.height) {
				l = this.height - n - 6;
			}

			k /= scale;
			l /= scale;

			matrices.push();

			matrices.translate(-k, -l, 0);
			matrices.scale(scale, scale, 0);

			k /= scale;
			l /= scale;

			matrices.translate(k, l, 0);

			int offset = 10;

			float startX = (k - offset) / (this.width / this.scale);
			float startY = 1F - (l + n + offset) / (this.height / this.scale);
			float endX = (k + i + offset) / (this.width / this.scale);
			float endY = 1F - (l - offset) / (this.height / this.scale);

			if (l - offset < 0) {
				float dY = n + offset + this.textRenderer.fontHeight * 3;
				matrices.translate(0, dY, 0);
				startY -= (dY / this.height) * scale;
				endY -= (dY / this.height) * scale;
			}

			if (k - offset < 0) {
				float dX = 3 - (k - offset);
				matrices.translate(dX, 0, 0);
				startX += (dX / this.width) * scale;
				endX += (dX / this.width) * scale;
			}

			if (k + i + offset > (this.width / scale)) {
				float dX = -(k + i + offset - (this.width / scale)) - 3;
				matrices.translate(dX, 0, 0);
				startX += (dX / this.width) * scale;
				endX += (dX / this.width) * scale;
			}

			BLUR.setUniformValue("Progress", 1F);
			BLUR.setUniformValue("Radius", 4F);
			BLUR.setUniformValue("Start", startX, startY);
			BLUR.setUniformValue("End", endX, endY);
			BLUR.render(1F);

			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferBuilder = tessellator.getBuffer();
			bufferBuilder.begin(7, VertexFormats.POSITION_COLOR);
			Matrix4f matrix4f = matrices.peek().getModel();

			int color = 0x80000000;

			if (hovered instanceof ValueEntry) {
				color = ((ValueEntry<?>) hovered).hasError() ? 0xB0800000 : color;
			}

			fill(matrix4f, bufferBuilder, k - offset, l - offset, k + i + offset, l + n + offset, 400, color);
			RenderSystem.enableDepthTest();
			RenderSystem.disableTexture();
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			bufferBuilder.end();
			BufferRenderer.draw(bufferBuilder);
			RenderSystem.disableBlend();
			RenderSystem.enableTexture();
			VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
			matrices.translate(0.0D, 0.0D, 400.0D);

			for(int s = 0; s < lines.size(); ++s) {
				OrderedText orderedText2 = lines.get(s);
				if (orderedText2 != null) {
					this.textRenderer.draw(orderedText2, k, l, -1, true, matrix4f, immediate, false, 0, 15728880);
				}

				if (s == 0) {
					l += 2;
				}

				l += 10;
			}

			immediate.draw();
			matrices.pop();
		}
	}

	public float getScale() {
		return this.scale;
	}
}
