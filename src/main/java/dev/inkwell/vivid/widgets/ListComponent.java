package dev.inkwell.vivid.widgets;

import dev.inkwell.vivid.screen.ConfigScreen;

public abstract class ListComponent extends WidgetComponent {
    protected final int index;

    public ListComponent(ConfigScreen parent, int x, int y, int width, int height, int index) {
        super(parent, x, y, width, height);
        this.index = index;
    }
}
