package dev.inkwell.vivid.util;

import net.minecraft.text.TranslatableText;

import java.util.function.BooleanSupplier;

public enum Enabled implements BooleanSupplier, Translatable {
	ON(true),
	OFF(false);

	public final boolean value;

	Enabled(boolean value) {
		this.value = value;
	}

	@Override
	public boolean getAsBoolean() {
		return this.value;
	}

	@Override
	public TranslatableText getText() {
		return new TranslatableText("vivid.enabled." + this.value);
	}
}
