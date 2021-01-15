package dev.monarkhes.vivid.util;

import java.util.function.Supplier;

public abstract class StronglyTypedCollection<K, V, I> implements Iterable<I> {
    protected final Class<V> valueClass;
    protected final Supplier<V> defaultValue;

    public StronglyTypedCollection(Class<V> valueClass, Supplier<V> defaultValue) {
        this.valueClass = valueClass;
        this.defaultValue = defaultValue;
    }

    public final Class<V> getValueClass() {
        return this.valueClass;
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
