package dev.inkwell.vivid.util;

import dev.inkwell.vivid.builders.ValueEntryBuilder;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

public class Table<V> extends StronglyTypedCollection<String, V> {
    private final Map<String, V> map;
    private List<Pair<String, V>> iterList;

    public Table(Class<V> valueClass, Supplier<V> defaultValue, ValueEntryBuilder<V> valueEntryBuilder) {
        super(valueClass, defaultValue, valueEntryBuilder);
        this.map = new LinkedHashMap<>();
    }

    public Table<V> add(String key, V value) {
        this.map.put(key, value);
        this.iterList = null;
        return this;
    }

    @Override
    public void addEntry() {
        this.map.put("", this.defaultValue.get());
        this.iterList = null;
    }

    @Override
    public V get(String key) {
        return this.map.get(key);
    }

    @Override
    public void put(String key, V v) {
        this.map.put(key, v);
        this.iterList = null;
    }

    @NotNull
    @Override
    public Iterator<Pair<String, V>> iterator() {
        if (this.iterList == null) {
            this.iterList = new ArrayList<>(this.map.size());

            for (Map.Entry<String, V> entry : this.map.entrySet()) {
                this.iterList.add(new Pair<>(entry.getKey(), entry.getValue()));
            }
        }

        return this.iterList.iterator();
    }
}
