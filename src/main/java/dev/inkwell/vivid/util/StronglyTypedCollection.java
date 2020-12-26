package dev.inkwell.vivid.util;

import dev.inkwell.vivid.builders.WidgetComponentBuilder;

import java.util.function.Supplier;

public abstract class StronglyTypedCollection<K, V, I> implements Iterable<I> {
    protected final Class<V> valueClass;
    protected final Supplier<V> defaultValue;
    protected final WidgetComponentBuilder<V> builder;

    public StronglyTypedCollection(Class<V> valueClass, Supplier<V> defaultValue, WidgetComponentBuilder<V> builder) {
        this.valueClass = valueClass;
        this.defaultValue = defaultValue;
        this.builder = builder;

    }

    public final WidgetComponentBuilder<V> getBuilder() {
        return this.builder;
    }

    public abstract void addEntry();
    public abstract V get(K key);

    public Supplier<V> getDefaultValue() {
        return this.defaultValue;
    }

    public abstract void put(K key, V v);

    public abstract void remove(int index);

    public abstract int size();
}
