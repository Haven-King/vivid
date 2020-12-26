package dev.inkwell.vivid.util;

import dev.inkwell.vivid.builders.WidgetComponentBuilder;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

public class Table<T> extends StronglyTypedCollection<Integer, T, Table.Entry<String, T>> {
    protected final List<Entry<String, T>> list = new ArrayList<>();

    public Table(Class<T> valueClass, Supplier<T> defaultValue, WidgetComponentBuilder<T> builder, Entry<String, T>... values) {
        super(valueClass, defaultValue, builder);
        this.list.addAll(Arrays.asList(values));
    }

    public Table(Table<T> other) {
        super(other.valueClass, other.defaultValue, other.builder);
        this.copy(other);
    }

    public void copy(Table<T> other) {
        this.list.clear();
        this.list.addAll(other.list);
    }

    @Override
    public void addEntry() {
        this.list.add(new Entry<>("", this.defaultValue.get()));
    }

    @Override
    public T get(Integer key) {
        return this.list.get(key).getValue();
    }

    @Override
    public void put(Integer key, T value) {
        list.get(key).setValue(value);
    }

    @Override
    public void remove(int index) {
        this.list.remove(index);
    }

    @Override
    public int size() {
        return this.list.size();
    }

    @NotNull
    @Override
    public Iterator<Entry<String, T>> iterator() {
        return this.list.iterator();
    }

    public void setKey(int index, String v) {
        this.list.get(index).setKey(v);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Table[");

        for (int i = 0; i < this.list.size(); ++i) {
            builder.append("(")
                    .append(this.list.get(i).key)
                    .append(",")
                    .append(this.list.get(i).value)
                    .append(")");

            if (i < this.list.size() - 1) {
                builder.append(", ");
            }
        }

        builder.append(']');

        return builder.toString();
    }

    public static class Entry<K, V> {
        private K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }
}
