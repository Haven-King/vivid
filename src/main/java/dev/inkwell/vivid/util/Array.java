package dev.inkwell.vivid.util;

import dev.inkwell.vivid.builders.WidgetComponentBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

public class Array<T> extends StronglyTypedList<T, T> {
    @SafeVarargs
    public Array(Class<T> valueClass, Supplier<T> defaultValue, WidgetComponentBuilder<T> builder, T... values) {
        super(valueClass, defaultValue, builder, values);
    }

    public Array(Array<T> other) {
        super(other);
    }

    public void copy(StronglyTypedList<T, T> other) {
        this.list.clear();
        this.list.addAll(other.list);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return this.list.iterator();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Array[");

        for (int i = 0; i < this.list.size(); ++i) {
            builder.append(this.list.get(i));

            if (i < this.list.size() - 1) {
                builder.append(", ");
            }
        }

        builder.append(']');

        return builder.toString();
    }
}
