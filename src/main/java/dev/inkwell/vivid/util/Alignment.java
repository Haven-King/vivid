package dev.inkwell.vivid.util;

public enum Alignment {
    LEFT(-1), CENTER(0), RIGHT(1);

    public final int mod;

    Alignment(int mod) {
        this.mod = mod;
    }
}
