package dev.inkwell.vivid.util;

import dev.inkwell.vivid.builders.ValueEntryBuilder;
import net.minecraft.util.Pair;

import java.util.function.Supplier;

public abstract class StronglyTypedCollection<K, V> implements Iterable<Pair<K, V>> {
    protected final Class<V> valueClass;
    protected final Supplier<V> defaultValue;
    protected final ValueEntryBuilder<V> valueEntryBuilder;

    public StronglyTypedCollection(Class<V> valueClass, Supplier<V> defaultValue, ValueEntryBuilder<V> valueEntryBuilder) {
        this.valueClass = valueClass;
        this.defaultValue = defaultValue;
        this.valueEntryBuilder = valueEntryBuilder;

    }

    public final ValueEntryBuilder<V> getValueEntryBuilder() {
        return this.valueEntryBuilder;
    }

    public abstract void addEntry();
    public abstract V get(K key);

    public Supplier<V> getDefaultValue() {
        return this.defaultValue;
    }

    public abstract void put(K key, V v);
}
