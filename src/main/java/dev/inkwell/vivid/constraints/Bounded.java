package dev.inkwell.vivid.constraints;

import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Bounded<T extends Number> extends Constraint {
	boolean isWithinBounds(T value);
	void setMin(@Nullable T min);
	void setMax(@Nullable T max);
	@Nullable T getMin();
	@Nullable T getMax();

	default void setBounds(T min, T max) {
		setMin(min);
		setMax(max);
	}

	default void addConstraintTooltips(List<Text> tooltips) {
		if (getMin() != null) {
			tooltips.add(new TranslatableText("vivid.constraint.min", getMin()));
		}

		if (getMax() != null) {
			tooltips.add(new TranslatableText("vivid.constraint.max", getMax()));
		}
	}
}
