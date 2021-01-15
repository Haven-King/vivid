package dev.inkwell.vivid.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public abstract class StronglyTypedList<T, I> extends StronglyTypedCollection<Integer, T, I> {
    protected final List<T> list = new ArrayList<>();

    @SafeVarargs
    public StronglyTypedList(Class<T> valueClass, Supplier<T> defaultValue, T... values) {
        super(valueClass, defaultValue);
        this.list.addAll(Arrays.asList(values));
    }

    public StronglyTypedList(StronglyTypedList<T, I> other) {
        super(other.valueClass, other.defaultValue);
        this.copy(other);
    }

    protected abstract void copy(StronglyTypedList<T,I> other);

    @Override
    public void addEntry() {
        this.list.add(this.defaultValue.get());
    }

    @Override
    public T get(Integer key) {
        return this.list.get(key);
    }

    @Override
    public void put(Integer key, T value) {
        list.set(key, value);
    }

    @Override
    public void remove(int index) {
        this.list.remove(index);
    }

    @Override
    public int size() {
        return this.list.size();
    }
}
