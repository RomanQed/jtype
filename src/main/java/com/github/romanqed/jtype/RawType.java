package com.github.romanqed.jtype;

import java.lang.reflect.Type;

public final class RawType<T> implements JType<T> {
    private final Class<T> type;

    RawType(Class<T> type) {
        this.type = type;
    }

    @Override
    public Class<T> asClass() {
        return type;
    }

    @Override
    public Type asType() {
        return type;
    }

    @Override
    public String getTypeName() {
        return type.getTypeName();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        var rawType = (RawType<?>) object;
        return type.equals(rawType.type);
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
