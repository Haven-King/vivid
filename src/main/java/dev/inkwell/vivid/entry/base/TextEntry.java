package dev.inkwell.vivid.entry.base;

import dev.inkwell.vivid.constraints.Matches;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TickableElement;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class TextEntry<T> extends ValueEntry<T> implements TickableElement, Matches {
	protected String text;
	private int maxLength = 32;
	private int firstCharacterIndex;
	private int selectionStart;
	private int selectionEnd;
	private boolean selecting;
	private int focusedTicks;
	private String regex = null;
	private Predicate<String> textPredicate = this::matches;

	@SuppressWarnings("unchecked")
	public TextEntry(MutableText name, Supplier<?> defaultValue, Object value) {
		super(name, defaultValue, value);
		this.text = this.valueOf((T) value);
	}

	@Override
	public boolean holdsFocus() {
		return true;
	}

	@Override
	public void render(MatrixStack matrices, int index, int width, int y, int mouseX, int mouseY, float delta) {
		super.render(matrices, index, width, y, mouseX, mouseY, delta);

		TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

		int left = (int) (width - 3 - textRenderer.getWidth(this.text.substring(selectionStart)) * parent.getScale());
		float y1 = y + (int) ((this.getHeight() - textRenderer.fontHeight * parent.getScale()) / 2F) - 1;

		if (selectionStart != selectionEnd) {
			int right = (int) (width - 3 - textRenderer.getWidth(this.text.substring(selectionEnd)) * parent.getScale());
			fill(matrices, left, y1, right, y1 + 2 + textRenderer.fontHeight * parent.getScale(), 0xF002288, 0.5F);
		} else if (this.isFocused()) {
			if (this.focusedTicks / 6 % 2 == 0) {
				matrices.push();
				matrices.scale(0.5F, 1F, 1F);
				fill(matrices, left * 2 - 1, y1, left * 2, y1 + 2 + textRenderer.fontHeight * parent.getScale(), 0xFFFFFFFF, 1F);
				matrices.pop();
			}
		}

		renderValue(matrices, textRenderer, text, (int) (width - 3 - textRenderer.getWidth(text) * parent.getScale()), (int) y1, 0xFFFFFFFF, parent.getScale());
	}

	protected void renderValue(MatrixStack matrices, TextRenderer textRenderer, String text, int x, int y, int color, float scale) {
		draw(matrices, textRenderer, text, x, y, color, scale);
	}

	@Override
	public void tick() {
		this.focusedTicks++;
	}

	public void write(String string) {
		int i = Math.min(this.selectionStart, this.selectionEnd);
		int j = Math.max(this.selectionStart, this.selectionEnd);
		int k = this.maxLength - this.text.length() - (i - j);
		String string2 = SharedConstants.stripInvalidChars(string);
		int l = string2.length();
		if (k < l) {
			string2 = string2.substring(0, k);
			l = k;
		}

		this.text = (new StringBuilder(this.text)).replace(i, j, string2).toString();
		this.setSelectionStart(i + l);
		this.setSelectionEnd(this.selectionStart);
		this.onChanged(this.text);
	}

	protected abstract String valueOf(T value);

	protected abstract T emptyValue();

	@Override
	public String getDefaultValueAsString() {
		return this.valueOf(this.defaultValue.get());
	}

	public void setSelectionStart(int cursor) {
		this.selectionStart = MathHelper.clamp(cursor, 0, this.text.length());
	}

	public void setSelectionEnd(int i) {
		int j = this.text.length();
		this.selectionEnd = MathHelper.clamp(i, 0, j);
		TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		if (textRenderer != null) {
			if (this.firstCharacterIndex > j) {
				this.firstCharacterIndex = j;
			}

			int k = this.width;
			String string = textRenderer.trimToWidth(this.text.substring(this.firstCharacterIndex), k);
			int l = string.length() + this.firstCharacterIndex;
			if (this.selectionEnd == this.firstCharacterIndex) {
				this.firstCharacterIndex -= textRenderer.trimToWidth(this.text, k, true).length();
			}

			if (this.selectionEnd > l) {
				this.firstCharacterIndex += this.selectionEnd - l;
			} else if (this.selectionEnd <= this.firstCharacterIndex) {
				this.firstCharacterIndex -= this.firstCharacterIndex - this.selectionEnd;
			}

			this.firstCharacterIndex = MathHelper.clamp(this.firstCharacterIndex, 0, j);
		}
	}

	public String getSelectedText() {
		int i = Math.min(this.selectionStart, this.selectionEnd);
		int j = Math.max(this.selectionStart, this.selectionEnd);
		return this.text.substring(i, j);
	}

	public void setCursor(int cursor) {
		this.setSelectionStart(cursor);
		if (!this.selecting) {
			this.setSelectionEnd(this.selectionStart);
		}

		this.onChanged(this.text);
	}

	public void setCursorToStart() {
		this.setCursor(0);
	}

	public void setCursorToEnd() {
		this.setCursor(this.text.length());
	}

	public int getCursor() {
		return this.selectionStart;
	}

	public int getWordSkipPosition(int wordOffset) {
		return this.getWordSkipPosition(wordOffset, this.getCursor());
	}

	private int getWordSkipPosition(int wordOffset, int cursorPosition) {
		return this.getWordSkipPosition(wordOffset, cursorPosition, true);
	}

	private int getWordSkipPosition(int wordOffset, int cursorPosition, boolean skipOverSpaces) {
		int i = cursorPosition;
		boolean bl = wordOffset < 0;
		int j = Math.abs(wordOffset);

		for(int k = 0; k < j; ++k) {
			if (!bl) {
				int l = this.text.length();
				i = this.text.indexOf(32, i);
				if (i == -1) {
					i = l;
				} else {
					while(skipOverSpaces && i < l && this.text.charAt(i) == ' ') {
						++i;
					}
				}
			} else {
				while(skipOverSpaces && i > 0 && this.text.charAt(i - 1) == ' ') {
					--i;
				}

				while(i > 0 && this.text.charAt(i - 1) != ' ') {
					--i;
				}
			}
		}

		return i;
	}

	private void moveCursor(int offset) {
		this.setCursor(this.getCursorLocation(offset));
	}

	private int getCursorLocation(int offset) {
		return Util.moveCursor(this.text, this.selectionStart, offset);
	}

	public void eraseWords(int wordOffset) {
		if (!this.text.isEmpty()) {
			if (this.selectionEnd != this.selectionStart) {
				this.write("");
			} else {
				this.eraseCharacters(this.getWordSkipPosition(wordOffset) - this.selectionStart);
			}
		}
	}

	public void eraseCharacters(int characterOffset) {
		if (!this.text.isEmpty()) {
			if (this.selectionEnd != this.selectionStart) {
				this.write("");
			} else {
				int i = this.getCursorLocation(characterOffset);
				int j = Math.min(i, this.selectionStart);
				int k = Math.max(i, this.selectionStart);
				if (j != k) {
					this.text = (new StringBuilder(this.text)).delete(j, k).toString();
					this.onChanged(this.text);
					this.setCursor(j);
				}
			}
		}
	}

	private void erase(int offset) {
		if (Screen.hasControlDown()) {
			this.eraseWords(offset);
		} else {
			this.eraseCharacters(offset);
		}

	}

	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (!this.isFocused()) {
			return false;
		} else {
			this.selecting = Screen.hasShiftDown();
			if (Screen.isSelectAll(keyCode)) {
				this.setCursorToEnd();
				this.setSelectionEnd(0);
				return true;
			} else if (Screen.isCopy(keyCode)) {
				MinecraftClient.getInstance().keyboard.setClipboard(this.getSelectedText());
				return true;
			} else if (Screen.isPaste(keyCode)) {
				this.write(MinecraftClient.getInstance().keyboard.getClipboard());

				return true;
			} else if (Screen.isCut(keyCode)) {
				MinecraftClient.getInstance().keyboard.setClipboard(this.getSelectedText());
				this.write("");

				return true;
			} else {
				switch(keyCode) {
					case 259:
						this.selecting = false;
						this.erase(-1);
						this.selecting = Screen.hasShiftDown();
						return true;
					case 260:
					case 264:
					case 265:
					case 266:
					case 267:
					default:
						return false;
					case 261:
						this.selecting = false;
						this.erase(1);
						this.selecting = Screen.hasShiftDown();
						return true;
					case 262:
						if (Screen.hasControlDown()) {
							this.setCursor(this.getWordSkipPosition(1));
						} else {
							this.moveCursor(1);
						}

						return true;
					case 263:
						if (Screen.hasControlDown()) {
							this.setCursor(this.getWordSkipPosition(-1));
						} else {
							this.moveCursor(-1);
						}

						return true;
					case 268:
						this.setCursorToStart();
						return true;
					case 269:
						this.setCursorToEnd();
						return true;
				}
			}
		}
	}

	private void onChanged(String newText) {
		if (newText.isEmpty()) {
			this.setValue(this.emptyValue());
		} else {
			Optional<T> value = this.parse(newText);
			value.ifPresent(this::setValue);
		}
	}

	public boolean charTyped(char chr, int keyCode) {
		if (!this.isFocused()) {
			return false;
		} else if (SharedConstants.isValidChar(chr)) {
			this.write(Character.toString(chr));

			return true;
		} else {
			return false;
		}
	}

	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		boolean bl = super.mouseClicked(mouseX, mouseY, button);
		this.setFocused(bl);

		if (this.isFocused() && bl && button == 0) {
			int i = MathHelper.floor(mouseX) - this.getX();
			if (this.isFocused()) {
				i -= 4;
			}

			TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
			String string = textRenderer.trimToWidth(this.text.substring(this.firstCharacterIndex), this.width);
			this.setCursor(textRenderer.trimToWidth(string, i).length() + this.firstCharacterIndex);
			return true;
		} else {
			return false;
		}
	}

	protected abstract Optional<T> parse(String value);

	public TextEntry<T> setMaxLength(int length) {
		this.maxLength = length;
		return this;
	}

	public void setPredicate(Predicate<String> predicate) {
		this.textPredicate = string -> predicate.test(string) && passes();
	}

	@Override
	public void setRegex(String regex) {
		this.regex = regex;
	}

	@Override
	public boolean hasError() {
		return !passes();
	}

	@Override
	public boolean passes() {
		return matches(this.text) && (this.textPredicate == null || this.textPredicate.test(this.text));
	}

	@Override
	public boolean matches(String value) {
		return regex == null || value.matches(regex);
	}

	@Override
	public @Nullable String getRegex() {
		return regex;
	}
}
