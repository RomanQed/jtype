package com.github.romanqed.jtype;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Arrays;

final class WildcardTypeImpl implements WildcardType, Serializable {
    // UID
    private static final long serialVersionUID = 1786018160;

    // According to the current specification only one bound is allowed,
    // however for future changes this implementation will allow for multiple bounds
    private final Type[] uppers;
    private final Type[] lowers;

    WildcardTypeImpl(Type[] uppers, Type[] lowers) {
        this.uppers = uppers;
        this.lowers = lowers;
    }

    @Override
    public Type[] getUpperBounds() {
        return uppers.clone();
    }

    @Override
    public Type[] getLowerBounds() {
        return lowers.clone();
    }

    @Override
    public String getTypeName() {
        // Implemented for current language spec
        if (lowers.length != 0) {
            return "? super " + TypeUtil.toString(lowers[0]);
        }
        if (uppers[0] == Object.class) {
            return "?";
        }
        return "? extends " + TypeUtil.toString(uppers[0]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WildcardTypeImpl)) return false;
        var that = (WildcardTypeImpl) o;
        return Arrays.equals(uppers, that.uppers) && Arrays.equals(lowers, that.lowers);
    }

    @Override
    public int hashCode() {
        // Magic 31 hash
        return 31 * Arrays.hashCode(uppers) + Arrays.hashCode(lowers);
    }

    @Override
    public String toString() {
        return getTypeName();
    }
}
