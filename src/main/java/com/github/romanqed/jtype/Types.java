package com.github.romanqed.jtype;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

public final class Types {

    public <T> JType<T> of(Class<T> type) {
        Objects.requireNonNull(type);
        return new RawType<>(type);
    }

    @SuppressWarnings("unchecked")
    public <T> JType<T> of(ParameterizedType type) {
        Objects.requireNonNull(type);
        var clazz = (Class<T>) type.getRawType();
        return new ParametrizedType<>(clazz, type);
    }

    @SuppressWarnings("unchecked")
    public <T> JType<T> of(GenericArrayType type) {
        var clazz = (Class<T>) TypeUtil.getRawType(type);
        return new ArrayType<>(clazz, type);
    }

    public <T> JType<T> of(Type type) {
        return null;
    }
}
