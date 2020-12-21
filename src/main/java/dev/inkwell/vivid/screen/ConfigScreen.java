package dev.inkwell.vivid.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.inkwell.vivid.DrawableExtensions;
import dev.inkwell.vivid.constraints.Constraint;
import dev.inkwell.vivid.entry.base.ValueEntry;
import dev.inkwell.vivid.util.Group;
import dev.inkwell.vivid.entry.base.ListEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TickableElement;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.*;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.math.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

// TODO: This class needs to be cleaned up/split up in general
public class ConfigScreen extends Screen implements DrawableExtensions {
	private final Screen parent;
	private final List<Group<Group<ListEntry>>> categories;
	private final int categoryWidth;

	private ScreenStyle style = ScreenStyle.DEFAULT;

	private int activeCategory = 0;
	private int scrollAmount = 0;

	private double clickedX;
	private float lastTickDelta;

	private int lastY = 0;

	private List<Text> tooltips = new ArrayList<>();
	private ListEntry hovered = null;

	// TODO: Better error hoisting?
	@SuppressWarnings({"FieldCanBeLocal", "unused"})
	private boolean hasError = false;

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

		for (int i = 0; i < categories.size(); ++i) {
			int categoryId = i;
			ButtonWidget button = new CategoryButtonWidget(
					this,
					(width - categoryWidth * categories.size()) / 2 + categoryWidth * i,
					0,
					categoryWidth,
					12,
					categories.get(i).getName(),
					(b) -> this.setActiveCategory(categoryId), (a, b, c, d) -> {});

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

		this.lastTickDelta = tickDelta;
		this.renderBackground(matrices);

		int contentHeight = lastY - this.height / 8;
		int visibleHeight = this.height - this.height / 8;

		if (contentHeight > visibleHeight) {
			// TODO: Fix scrollbar size/position
			float ratio = visibleHeight / (float) contentHeight;
			int startX = (width / 4) * 3 + 2;
			int startY = (int) (this.height / 8 - scrollAmount * ratio) + 9;
			int height = (int) ((int) (this.height / 8 + visibleHeight * ratio / 2) - scrollAmount * ratio) + 9 - startY;
			boolean hovered = mouseX >= startX && mouseY >= startY && mouseX <= startX + width && mouseY <= startY + height;
			this.style.renderScrollbar(matrices, startX, startY, 3, height, false, hovered);
		}

		this.style.renderDecorations(matrices, mouseX, mouseY, tickDelta, this.width, this.height);

		super.render(matrices, mouseX, mouseY, tickDelta);

		matrices.push();
		matrices.translate(width / 4F, 30, 0F);

		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		Window window = client.getWindow();
		GL11.glScissor(0, 0, window.getFramebufferWidth(), (window.getFramebufferHeight() / 10) * 9);

		int y = 0;
		Group<Group<ListEntry>> sections = categories.get(activeCategory);
		int sectionColor = this.style.sectionColor.getColor() != null
				? this.style.sectionColor.getColor().getRgb()
				: 0xFFFFFFFF;

		this.hasError = false;
		this.hovered = null;

		int focusedJ = 0;
		int focusedY = 0;

		for (int i = 0; i < sections.size(); ++i) {
			Group<ListEntry> section = sections.get(i);
			this.draw(matrices, this.textRenderer, section.getName(), 0, y + scrollAmount, sectionColor, 0.5F);

			y += 10;

			for (int j = 0; j < section.size(); ++j) {
				ListEntry entry = section.get(j);

				if (this.getFocused() instanceof ListEntry && entry == this.getFocused()) {
					focusedJ = j;
					focusedY = y + scrollAmount;
				} else {
					entry.render(matrices, j, width / 2, y + scrollAmount, mouseX - width / 4, mouseY - 30, tickDelta);
				}

				if (mouseY > 30 && entry.isMouseOver(mouseX - width / 4F, mouseY - 30) && (entry == this.getFocused() || this.getFocused() == null)) {
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
			((ListEntry) this.getFocused()).render(matrices, focusedJ, width / 2, focusedY, mouseX - width / 4, mouseY - 30, tickDelta);
		}

		matrices.pop();

		lastY = y;

		if (!tooltips.isEmpty()) {
			this.renderTooltip(matrices, tooltips, mouseX, mouseY);
			tooltips.clear();
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
		return (int) -(this.lastY - this.height + 30 + height / 8F);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if (clickedX > (width / 4F) * 3 + 2 && clickedX < (width / 4F) * 3 + 5) {
			return mouseScrolled(mouseX, mouseY, -deltaY);
		} else {
			for (Group<ListEntry> section : categories.get(activeCategory)) {
				for (ListEntry entry : section) {
					if (entry.mouseDragged(mouseX - width / 4F, mouseY - 30, button, deltaX, deltaY)) {
						return true;
					}
				}
			}
		}

		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
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
			if (getFocused() == null || !getFocused().isMouseOver(mouseX - width / 4F, mouseY - 30)) {
				for (Group<ListEntry> section : categories.get(activeCategory)) {
					for (ListEntry entry : section) {
						if (entry.holdsFocus()) {
							entry.setFocused(entry.isMouseOver(mouseX - width / 4F, mouseY - 30));

							if (entry.isFocused()) {
								this.setFocused(entry);
							}
						}

						bl = bl || entry.mouseClicked(mouseX - width / 4F, mouseY - 30, button);
					}
				}
			} else {
				bl = getFocused().mouseClicked(mouseX - width / 4F, mouseY - 30, button);
			}
		}

		if (!bl) {
			this.setFocused(null);
		}

		return bl;
	}

	@Override
	public boolean charTyped(char chr, int keyCode) {
		if (this.getFocused() != null) {
			return this.getFocused().charTyped(chr, keyCode);
		}

		return false;
	}

	@Override
	public void tick() {
		if (this.getFocused() != null && this.getFocused() instanceof TickableElement) {
			((TickableElement) this.getFocused()).tick();
		}
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		boolean bl = false;

		if (this.getFocused() != null) {
			bl = this.getFocused().keyPressed(keyCode, scanCode, modifiers);
		}

		return bl || super.keyPressed(keyCode, scanCode, modifiers);
	}

	public ScreenStyle getStyle() {
		return this.style;
	}

	public void addTooltips(List<Text> tooltips) {
		this.tooltips.addAll(tooltips);
	}

	@Override
	public void renderOrderedTooltip(MatrixStack matrices, List<? extends OrderedText> lines, int x, int y) {
		// TODO: Would be great to be able to blur the things behind the tooltips. Reach out to Pyrofab.
		if (!lines.isEmpty()) {
			int i = 0;

			for (OrderedText orderedText : lines) {
				int j = this.textRenderer.getWidth(orderedText);
				if (j > i) {
					i = j;
				}
			}

			float k = x - (i / 4F);
			float l = y - 10 - ((textRenderer.fontHeight * lines.size()) / 2F);
			int n = 8;
			if (lines.size() > 1) {
				n += 2 + (lines.size() - 1) * 10;
			}

			k /= 2;
			l /= 2;

			if (k + i > this.width) {
				k -= 28 + i;
			}

			if (l + n + 6 > this.height) {
				l = this.height - n - 6;
			}

			k *= 2;
			l *= 2;

			matrices.translate(-k, -l, 0);
			matrices.scale(0.5F, 0.5F, 0);

			k *= 2;
			l *= 2;

			matrices.translate(k, l, 0);

			matrices.push();
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferBuilder = tessellator.getBuffer();
			bufferBuilder.begin(7, VertexFormats.POSITION_COLOR);
			Matrix4f matrix4f = matrices.peek().getModel();

			int color = 0xB0000000;

			if (hovered instanceof ValueEntry) {
				color = ((ValueEntry<?>) hovered).hasError() ? 0xB0800000 : color;
			}

			int offset = 10;
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
}
