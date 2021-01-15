package dev.inkwell.vivid.widgets;

public interface Mutable {
    void save();
    void reset();
    boolean hasChanged();
    boolean hasError();
}
