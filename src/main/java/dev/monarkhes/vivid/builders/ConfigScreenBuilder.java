package dev.monarkhes.vivid.builders;

import dev.monarkhes.vivid.screen.ConfigScreen;
import dev.monarkhes.vivid.util.Group;
import dev.monarkhes.vivid.widgets.WidgetComponent;

import java.util.List;

public interface ConfigScreenBuilder {
    List<Group<Group<WidgetComponent>>> build(ConfigScreen parent, int contentLeft, int contentWidth, int y);
}
