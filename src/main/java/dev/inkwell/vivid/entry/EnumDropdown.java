package dev.inkwell.vivid.entry;

import dev.inkwell.vivid.entry.base.DropdownEntry;
import dev.inkwell.vivid.util.Translatable;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;

import java.util.function.Supplier;

public class EnumDropdown<T extends Enum<T>> extends DropdownEntry<T> {
	public EnumDropdown(MutableText name, Supplier<?> defaultValue, Object value) {
		super(name, defaultValue, value);
		this.setPossibleValues(this.getValue().getDeclaringClass().getEnumConstants());
	}

	@Override
	protected MutableText valueOf(T value) {
		return value instanceof Translatable ? ((Translatable) value).getText() : new LiteralText(value.name());
	}
}
