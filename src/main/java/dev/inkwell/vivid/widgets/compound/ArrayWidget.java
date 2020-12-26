package dev.inkwell.vivid.widgets.compound;

import dev.inkwell.vivid.builders.ConfigScreenBuilder;
import dev.inkwell.vivid.screen.ConfigScreen;
import dev.inkwell.vivid.util.Array;
import dev.inkwell.vivid.util.Group;
import dev.inkwell.vivid.widgets.Mutable;
import dev.inkwell.vivid.widgets.TextButton;
import dev.inkwell.vivid.widgets.WidgetComponent;
import dev.inkwell.vivid.widgets.containers.RowContainer;
import dev.inkwell.vivid.widgets.value.ValueWidgetComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ArrayWidget<T> extends ValueWidgetComponent<Array<T>> implements ConfigScreenBuilder {
    private final Text name;
    private final float scale;

    private ConfigScreen screen;
    private boolean changed;

    public ArrayWidget(ConfigScreen parent, int x, int y, int width, int height, Supplier<@NotNull Array<T>> defaultValueSupplier, Consumer<Array<T>> changedListener, Consumer<Array<T>> saveConsumer, @NotNull Array<T> value, Text name) {
        super(parent, x, y, width, height, defaultValueSupplier, changedListener, saveConsumer, new Array<>(value));
        this.name = name;
        this.scale = this.height / parent.getScale();
    }

    @Override
    public void renderContents(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        int width = textRenderer.getWidth("▶");

        drawCenteredString(
                matrixStack,
                textRenderer,
                "▶",
                this.x + this.width - 3 - width * this.parent.getScale(),
                (int) (this.y + (this.height - textRenderer.fontHeight * this.parent.getScale()) / 2F),
                0xFFFFFFFF,
                this.parent.getScale()
        );
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isMouseOver(mouseX, mouseY)) {
            MinecraftClient.getInstance().openScreen((this.screen = new ConfigScreen(this.parent, this)));
        }

        return false;
    }

    @Override
    public boolean hasError() {
        return false;
    }

    @Override
    public void renderBackground(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {

    }

    @Override
    protected Text getDefaultValueAsText() {
        return new LiteralText(this.getDefaultValue().toString());
    }

    @Override
    public List<Group<Group<WidgetComponent>>> build(ConfigScreen parent, int contentLeft, int contentWidth, int y) {
        Group<WidgetComponent> section = new Group<>();
        List<Group<Group<WidgetComponent>>> categories = Collections.singletonList(new Group<>((MutableText) this.name));
        categories.get(0).add(section);

        Array<T> array = this.getValue();

        int i = 0;
        int dY = y;
        int height = (int) (this.scale * parent.getScale());
        for (T value : array) {
            int index = i++;

            WidgetComponent remove = new TextButton(
                    parent, 0, 0, height, height, 0, new LiteralText("✕"), button ->
                {
                    this.getValue().remove(index);
                    this.screen.setProvider(this);
                    this.changed = true;
                    return true;
                }
            ) {
                @Override
                protected int highlightColor() {
                    return 0x80FF0000;
                }
            };

            WidgetComponent up = new TextButton(
                    parent, 0, 0, height, height, 0, new LiteralText("▲"), button ->
                {
                    if (index > 0 && array.size() >= 2) {
                        T temp = array.get(index);
                        array.put(index, array.get(index - 1));
                        array.put(index - 1, temp);
                        this.screen.setProvider(this);
                        this.changed = true;
                        return true;
                    } else {
                        return false;
                    }
                }
            );

            WidgetComponent down = new TextButton(
                    parent, 0, 0, height, height, 0, new LiteralText("▼"), button ->
                {
                    if (index < array.size() - 1 && array.size() >= 2) {
                        T temp = array.get(index);
                        array.put(index, array.get(index + 1));
                        array.put(index + 1, temp);
                        this.screen.setProvider(this);
                        this.changed = true;
                        return true;
                    } else {
                        return false;
                    }
                }
            );

            WidgetComponent widget = array.getBuilder().build(
                    parent,
                    0,
                    dY,
                    contentWidth - height * 3,
                    height,
                    array.getDefaultValue(),
                    v -> {
                        array.put(index, v);
                    },
                    v -> {},
                    value
            );

            section.add(new RowContainer(parent, contentLeft, dY, index, false, remove, widget, up, down));
            dY += widget.getHeight();
        }

        section.add(new AddButton(parent, contentLeft, dY, contentWidth, height, 0x40000000, new LiteralText("+"), button -> {
            this.getValue().addEntry();
            this.screen.setProvider(this);
            this.changed = true;
            return true;
        }));

        return categories;
    }

    class AddButton extends TextButton implements Mutable {
        public AddButton(ConfigScreen parent, int x, int y, int width, int height, int color, MutableText text, Action onClick) {
            super(parent, x, y, width, height, color, text, onClick);
        }

        @Override
        public void save() {
            ArrayWidget.this.save();
        }

        @Override
        public void reset() {

        }

        @Override
        public boolean hasChanged() {
            return ArrayWidget.this.changed;
        }

        @Override
        public boolean hasError() {
            return false;
        }
    }
}
