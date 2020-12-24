package dev.inkwell.vivid.entry;

import dev.inkwell.vivid.entry.base.EntryType;
import dev.inkwell.vivid.entry.base.ListEntry;
import dev.inkwell.vivid.entry.base.ValueEntry;
import dev.inkwell.vivid.screen.ConfigScreen;
import dev.inkwell.vivid.util.Array;
import dev.inkwell.vivid.util.Group;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ArrayEntry<V> extends ValueEntry<Array<V>> {
    private ConfigScreen screen;
    private boolean changed;

    public ArrayEntry(MutableText name, Supplier<?> defaultValue, Consumer<?> saveConsumer, Object value, EntryType entryType) {
        super(name, defaultValue, saveConsumer, value, entryType);
    }

    @Override
    public boolean hasError() {
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) {
            MinecraftClient.getInstance().openScreen((this.screen = new ConfigScreen(this.parent, this.build()).withStyle(this.parent.getStyle())));
            return true;
        }

        return false;
    }

    @Override
    protected void renderContents(MatrixStack matrices, int index, int width, int y, int mouseX, int mouseY, float delta) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        draw(matrices, textRenderer, "▶", width - 6, y + (getHeight() - textRenderer.fontHeight) / 2 + 1, 0xFFFFFFFF, 0.5F);
    }

    @Override
    public boolean hasChanged() {
        return false;
    }

    @SuppressWarnings("unchecked")
    private List<Group<Group<ListEntry>>> build() {
        Group<ListEntry> section = new Group<>();
        List<Group<Group<ListEntry>>> categories = Collections.singletonList(new Group<>((MutableText) this.getName()));
        categories.get(0).add(section);

        Array<V> array = this.getValue();
        for (Pair<Integer, V> entry : array) {
            ValueEntry<V> valueEntry = array.getValueEntryBuilder().build(
                    new LiteralText("" + entry.getLeft()),
                    (Supplier<Object>) array.getDefaultValue(),
                    v -> {},
                    entry.getRight(),
                    EntryType.ARRAY
            );

            valueEntry.setChangedListener(v -> {
                array.put(entry.getLeft(), (V) v);
                this.changed = true;
            });

            section.add(valueEntry);
        }

        section.add(new AddButton());

        return categories;
    }

    public class AddButton extends ValueEntry<Void> {
        protected AddButton() {
            super(LiteralText.EMPTY.copy(), null, v -> {}, null, EntryType.ARRAY);
        }

        @Override
        public void renderContents(MatrixStack matrices, int index, int width, int y, int mouseX, int mouseY, float delta) {
//            this.y = y;
//            this.width = width;
//
//            int color1 = index % 2 == 0 ? 0x44FFFFFF : 0x22888888;
//
//            this.updateHoverOpacity(mouseX, mouseY);
//
//            DrawableHelper.fill(matrices, 0, y, width, y + getHeight(), color1);
//
//            renderHighlight(matrices, 0, width, y, 0xFFFFFFFF);
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            this.drawCenteredText(matrices, textRenderer, new LiteralText("+"), width / 2F, y + (int) ((this.getHeight() - textRenderer.fontHeight * parent.getScale() * 2) / 2F), 0xFFFFFFFF, parent.getScale() * 2);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (super.mouseClicked(mouseX, mouseY, button)) {
                ArrayEntry.this.getValue().addEntry();
                ArrayEntry.this.screen.setCategories(ArrayEntry.this.build());
                ArrayEntry.this.changed = true;
                return true;
            }

            return false;
        }

        @Override
        public boolean hasChanged() {
            return ArrayEntry.this.changed;
        }

        @Override
        public void save() {
            ArrayEntry.this.save();
            ArrayEntry.this.changed = false;
        }

        @Override
        public int getHeight() {
            return (int) (30 * parent.getScale());
        }

        @Override
        public boolean hasError() {
            return false;
        }

        @Override
        public void addTooltipsToList(List<Text> tooltips) {

        }
    }
}
