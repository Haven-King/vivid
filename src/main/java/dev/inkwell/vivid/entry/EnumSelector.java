package dev.inkwell.vivid.entry;

import dev.inkwell.vivid.entry.base.ValueEntry;
import dev.inkwell.vivid.util.Translatable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class EnumSelector<T extends Enum<T>> extends ValueEntry<T> {
	public EnumSelector(MutableText name, Supplier<?> defaultValue, Consumer<?> saveConsumer, Object value) {
		super(name, defaultValue, saveConsumer, value);
	}

	@Override
	public void render(MatrixStack matrices, int index, int width, int y, int mouseX, int mouseY, float delta) {
		super.render(matrices, index, width, y, mouseX, mouseY, delta);

		TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

		Enum<T>[] enums = this.getValue().getDeclaringClass().getEnumConstants();
		float segmentSize = (width / 2F) / enums.length;
		for (int i = 0; i < enums.length; ++i) {
			float x1 = width / 2F + segmentSize * i;
			float x2 = i < enums.length - 1 ? segmentSize * (i + 1) + width / 2F : width;

			boolean b = enums[i] == this.getValue();
			fill(matrices, x1, y, x2, y + this.getHeight(), 0xFFFFFFFF, b ?  0.25F : 0F);

			float y1 = y + (int) ((this.getHeight() - textRenderer.fontHeight * parent.getScale()) / 2F) - 1;

			MutableText text = enums[i] instanceof Translatable ? ((Translatable) enums[i]).getText() : new TranslatableText(enums[i].name());
			drawCenteredText(matrices, textRenderer, text.styled(style -> style.withUnderline(b)), x1 + segmentSize / 2, y1, 0xFFFFFFFF, parent.getScale());
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (super.mouseClicked(mouseX, mouseY, button)) {
			Enum<T>[] enums = this.getValue().getDeclaringClass().getEnumConstants();

			int i = (int) (((mouseX - width / 2) / (width / 2)) * enums.length);
			this.setValue((T) enums[i]);

			return true;
		}

		return false;
	}

	@Override
	public boolean hasError() {
		return false;
	}
}
