package dev.inkwell.vivid.widgets.compound;

import dev.inkwell.vivid.builders.ConfigScreenBuilder;
import dev.inkwell.vivid.builders.WidgetComponentBuilder;
import dev.inkwell.vivid.screen.ConfigScreen;
import dev.inkwell.vivid.util.Alignment;
import dev.inkwell.vivid.util.Group;
import dev.inkwell.vivid.util.Table;
import dev.inkwell.vivid.widgets.Mutable;
import dev.inkwell.vivid.widgets.TextButton;
import dev.inkwell.vivid.widgets.WidgetComponent;
import dev.inkwell.vivid.widgets.containers.RowContainer;
import dev.inkwell.vivid.widgets.value.ValueWidgetComponent;
import dev.inkwell.vivid.widgets.value.entry.StringEntryWidget;
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

public class TableWidget<T> extends ValueWidgetComponent<Table<T>> implements ConfigScreenBuilder {
    private final Text name;
    private final WidgetComponentBuilder<T> builder;
    private final float scale;
    private final boolean mutable;

    private ConfigScreen screen;
    private boolean changed;

    public TableWidget(ConfigScreen parent, int x, int y, int width, int height, Supplier<@NotNull Table<T>> defaultValueSupplier, Consumer<Table<T>> changedListener, Consumer<Table<T>> saveConsumer, @NotNull Table<T> value, Text name, WidgetComponentBuilder<T> builder, boolean mutable) {
        super(parent, x, y, width, height, defaultValueSupplier, changedListener, saveConsumer, new Table<>(value));
        this.name = name;
        this.scale = this.height / parent.getScale();
        this.builder = builder;
        this.mutable = mutable;
    }

    public TableWidget(ConfigScreen parent, int x, int y, int width, int height, Supplier<@NotNull Table<T>> defaultValueSupplier, Consumer<Table<T>> changedListener, Consumer<Table<T>> saveConsumer, @NotNull Table<T> value, Text name, WidgetComponentBuilder<T> builder) {
        this(parent, x, y, width, height, defaultValueSupplier, changedListener, saveConsumer, value, name, builder, true);
    }

    @Override
    public List<Group<Group<WidgetComponent>>> build(ConfigScreen parent, int contentLeft, int contentWidth, int y) {
        Group<WidgetComponent> section = new Group<>();
        List<Group<Group<WidgetComponent>>> categories = Collections.singletonList(new Group<>((MutableText) this.name));
        categories.get(0).add(section);

        Table<T> table = this.getValue();

        int i = 0;
        int dY = y;
        int height = (int) (this.scale * parent.getScale());
        for (Table.Entry<String, T> value : table) {
            int index = i++;

            @SuppressWarnings("SuspiciousNameCombination")
            WidgetComponent remove = new TextButton(
                    parent, 0, 0, height, height, 0, new LiteralText("✕"), button ->
            {
                table.remove(index);
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

            WidgetComponent keyWidget = new StringEntryWidget(
                    parent,
                    0,
                    dY,
                    (contentWidth - height * (this.mutable ? 2 : 0)) / 2,
                    height,
                    Alignment.LEFT,
                    () -> "",
                    v -> table.setKey(index, v),
                    v -> {
                        this.changed = true;
                    },
                    value.getKey()
            );

            WidgetComponent valueWidget = this.builder.build(
                    parent,
                    0,
                    dY,
                    (contentWidth) / 2,
                    height,
                    table.getDefaultValue(),
                    v -> table.put(index, v),
                    v -> this.changed = true,
                    value.getValue()
            );

            if (this.mutable) {
                section.add(new RowContainer(parent, contentLeft, dY, index, false, remove, keyWidget, valueWidget));
            } else {
                section.add(new RowContainer(parent, contentLeft, dY, index, false, keyWidget, valueWidget));
            }

            dY += valueWidget.getHeight();
        }

        if (this.mutable) {
            section.add(new TextButton(parent, contentLeft, dY, contentWidth, height, 0x40000000, new LiteralText("+"), button -> {
                this.getValue().addEntry();
                this.screen.setProvider(this);
                this.changed = true;
                return true;
            }));
        }

        section.add(new Dummy());

        return categories;
    }

    @Override
    public boolean hasError() {
        return false;
    }

    @Override
    public void renderBackground(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isMouseOver(mouseX, mouseY)) {
            MinecraftClient.getInstance().openScreen((this.screen = new ConfigScreen(this.parent, this)));
        }

        return false;
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
    protected Text getDefaultValueAsText() {
        return new LiteralText(this.getDefaultValue().toString());
    }

    class Dummy extends WidgetComponent implements Mutable {
        public Dummy() {
            super(TableWidget.this.parent, 0, 0, 0, 0);
        }

        @Override
        public void save() {
            TableWidget.this.save();
        }

        @Override
        public void reset() {

        }

        @Override
        public boolean hasChanged() {
            return TableWidget.this.changed;
        }

        @Override
        public boolean hasError() {
            return false;
        }

        @Override
        public void renderBackground(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {

        }

        @Override
        public void renderContents(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {

        }
    }
}
