package dev.monarkhes.vivid.screen;

import net.minecraft.client.gui.Element;
import net.minecraft.text.Text;

import java.util.function.Consumer;

@FunctionalInterface
public interface TooltipAccess extends Element {
    void addTooltips(Consumer<Text> tooltipConsumer);
}
