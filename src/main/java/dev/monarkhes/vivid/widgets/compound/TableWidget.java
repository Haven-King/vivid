package dev.monarkhes.vivid.widgets.compound;

import dev.monarkhes.vivid.Category;
import dev.monarkhes.vivid.builders.ConfigScreenBuilder;
import dev.monarkhes.vivid.builders.WidgetComponentFactory;
import dev.monarkhes.vivid.screen.ConfigScreen;
import dev.monarkhes.vivid.util.Alignment;
import dev.monarkhes.vivid.util.Group;
import dev.monarkhes.vivid.widgets.Mutable;
import dev.monarkhes.vivid.widgets.TextButton;
import dev.monarkhes.vivid.widgets.WidgetComponent;
import dev.monarkhes.vivid.widgets.containers.RowContainer;
import dev.monarkhes.vivid.widgets.value.ValueWidgetComponent;
import dev.monarkhes.vivid.widgets.value.entry.StringEntryWidget;
import net.fabricmc.loader.api.config.util.Table;
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
    private final WidgetComponentFactory<T> builder;
    private final float scale;
    private final boolean mutable;

    private ConfigScreen screen;
    private boolean changed;

    public TableWidget(ConfigScreen parent, int x, int y, int width, int height, Supplier<@NotNull Table<T>> defaultValueSupplier, Consumer<Table<T>> changedListener, Consumer<Table<T>> saveConsumer, @NotNull Table<T> value, Text name, WidgetComponentFactory<T> builder, boolean mutable) {
        super(parent, x, y, width, height, defaultValueSupplier, changedListener, saveConsumer, new Table<>(value));
        this.name = name;
        this.scale = this.height / parent.getScale();
        this.builder = builder;
        this.mutable = mutable;
    }

    public TableWidget(ConfigScreen parent, int x, int y, int width, int height, Supplier<@NotNull Table<T>> defaultValueSupplier, Consumer<Table<T>> changedListener, Consumer<Table<T>> saveConsumer, @NotNull Table<T> value, Text name, WidgetComponentFactory<T> builder) {
        this(parent, x, y, width, height, defaultValueSupplier, changedListener, saveConsumer, value, name, builder, true);
    }

    @Override
    public List<Category> build(ConfigScreen parent, int contentLeft, int contentWidth, int y) {
        Group<WidgetComponent> section = new Group<>();
        List<Category> categories = Collections.singletonList(new Category((MutableText) this.name));
        categories.get(0).add(section);

        int i = 0;
        int dY = y;
        int height = (int) (this.scale * parent.getScale());
        for (Table.Entry<String, T> value : this.getValue()) {
            int index = i++;

            @SuppressWarnings("SuspiciousNameCombination")
            WidgetComponent remove = new TextButton(
                    parent, 0, 0, height, height, 0, new LiteralText("✕"), button ->
            {
                this.setValue(this.getValue().remove(index));
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
                    v -> this.setValue(this.getValue().setKey(index, v)),
                    v -> this.changed = true,
                    value.getKey()
            );

            WidgetComponent valueWidget = this.builder.build(
                    parent,
                    0,
                    dY,
                    (contentWidth) / 2,
                    height,
                    this.getValue().getDefaultValue(),
                    v -> this.setValue(this.getValue().set(index, v)),
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
                this.setValue(this.getValue().addEntry());
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
            this.parent.tryLeave(() -> {
                MinecraftClient.getInstance().openScreen((this.screen = new ConfigScreen(this.parent, this)));
            });
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
