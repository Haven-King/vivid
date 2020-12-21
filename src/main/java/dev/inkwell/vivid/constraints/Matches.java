package dev.inkwell.vivid.constraints;

import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Matches extends Constraint {
	boolean matches(String value);
	@Nullable String getRegex();
	void setRegex(String regex);

	@Override
	default void addConstraintTooltips(List<Text> tooltips) {
		if (getRegex() != null) {
			tooltips.add(new TranslatableText("vivid.constraint.matches", getRegex()));
		}
	}
}
