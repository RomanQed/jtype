package com.github.romanqed.jtype;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class JType<T> {
    private final Class<T> raw;
    private final Type type;

    JType(Class<T> raw, Type type) {
        this.raw = raw;
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    protected JType() {
        this.type = getTypeArgument();
        this.raw = (Class<T>) TypeUtil.getRawType(this.type);
    }

    private Type getTypeArgument() {
        var parent = getClass().getGenericSuperclass();
        if (!(parent instanceof ParameterizedType)) {
            throw new IllegalStateException("Missing type argument");
        }
        var parameterized = (ParameterizedType) parent;
        if (parameterized.getRawType() != JType.class) {
            throw new IllegalStateException("JType must be inherited directly");
        }
        return parameterized.getActualTypeArguments()[0];
    }

    public Class<T> getRawType() {
        return raw;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JType)) return false;
        var that = (JType<?>) o;
        return type.equals(that.type);
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
