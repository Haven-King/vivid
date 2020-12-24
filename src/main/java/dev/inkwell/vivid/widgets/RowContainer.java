package dev.inkwell.vivid.widgets;

import dev.inkwell.vivid.screen.ConfigScreen;
import org.jetbrains.annotations.NotNull;

public class RowContainer extends ComponentContainer{
    public RowContainer(ConfigScreen parent, int x, int y, @NotNull WidgetComponent child, WidgetComponent... children) {
        super(parent, child, children);
        this.x = x;
        this.y = y;
    }

    @Override
    protected void init(WidgetComponent child, WidgetComponent... children) {
        child.x = this.x;
        child.y = this.y;
        this.width = child.width;
        this.height = child.height;

        for (WidgetComponent thing : children) {
            thing.x = this.x + this.width;
            thing.y = this.y;

            this.width = this.width + thing.width;
            this.height = Math.max(this.height, thing.height);
        }
    }
}
