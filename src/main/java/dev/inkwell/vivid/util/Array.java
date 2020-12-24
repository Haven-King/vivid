package dev.inkwell.vivid.util;

import dev.inkwell.vivid.builders.ValueEntryBuilder;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

public class Array<T> extends StronglyTypedCollection<Integer, T> {
    private final List<T> list;
    private List<Pair<Integer, T>> iterList;

    @SafeVarargs
    public Array(Class<T> valueClass, Supplier<T> defaultValue, ValueEntryBuilder<T> valueEntryBuilder, T... values) {
        super(valueClass, defaultValue, valueEntryBuilder);
        this.list = new ArrayList<>();
        this.list.addAll(Arrays.asList(values));
    }

    public Array(Array<T> other) {
        super(other.valueClass, other.defaultValue, other.valueEntryBuilder);
        this.list = new ArrayList<>();
        this.copy(other);
    }

    public void copy(Array<T> other) {
        this.list.clear();
        this.list.addAll(other.list);
    }

    @Override
    public void addEntry() {
        this.list.add(this.defaultValue.get());
        this.iterList = null;
    }

    @Override
    public T get(Integer key) {
        return this.list.get(key);
    }

    @Override
    public void put(Integer key, T value) {
        list.set(key, value);
        this.iterList = null;
    }

    @NotNull
    @Override
    public Iterator<Pair<Integer, T>> iterator() {
        if (this.iterList == null) {
            iterList = new ArrayList<>(this.list.size());

            for (int i = 0; i < this.list.size(); ++i) {
                iterList.add(new Pair<>(i, this.list.get(i)));
            }
        }

        return iterList.iterator();
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
