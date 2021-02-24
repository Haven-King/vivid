package dev.monarkhes.vivid;

import dev.monarkhes.vivid.util.Group;
import dev.monarkhes.vivid.widgets.WidgetComponent;
import net.minecraft.text.MutableText;

public class Category extends Group<Group<WidgetComponent>> {
    private Runnable saveCallback = () -> {};

    public Category(MutableText name, Runnable saveCallback) {
        this(name);
        this.saveCallback = saveCallback;
    }

    public Category() {

    }

    public Category(MutableText name) {
        super(name);
    }

    public void save() {
        this.saveCallback.run();
    }
}
