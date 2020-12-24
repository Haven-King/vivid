package dev.inkwell.vivid.entry;

import dev.inkwell.vivid.entry.base.DropdownEntry;
import dev.inkwell.vivid.entry.base.EntryType;
import dev.inkwell.vivid.util.Translatable;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class EnumDropdown<T extends Enum<T>> extends DropdownEntry<T> {
	public EnumDropdown(MutableText name, Supplier<?> defaultValue, Consumer<?> saveConsumer, Object value, EntryType entryType) {
		super(name, defaultValue, saveConsumer, value, entryType);
		this.setPossibleValues(this.getValue().getDeclaringClass().getEnumConstants());
	}

	@Override
	protected MutableText valueOf(T value) {
		return value instanceof Translatable ? ((Translatable) value).getText() : new LiteralText(value.name());
	}
}
