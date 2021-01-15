package dev.inkwell.vivid.builders;

import dev.inkwell.vivid.screen.ConfigScreen;
import dev.inkwell.vivid.util.Group;
import dev.inkwell.vivid.widgets.WidgetComponent;

import java.util.List;

public interface ConfigScreenBuilder {
    List<Group<Group<WidgetComponent>>> build(ConfigScreen parent, int contentLeft, int contentWidth, int y);
}
