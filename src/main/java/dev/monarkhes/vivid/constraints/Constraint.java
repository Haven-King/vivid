package dev.monarkhes.vivid.constraints;

import net.minecraft.text.Text;

import java.util.List;

public interface Constraint {
	boolean passes();
	void addConstraintTooltips(List<Text> tooltips);
}
