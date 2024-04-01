package com.github.romanqed.jtype;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public final class ParametrizedType<T> implements JType<T> {
    private final Class<T> clazz;
    private final ParameterizedType type;

    ParametrizedType(Class<T> clazz, ParameterizedType type) {
        this.clazz = clazz;
        this.type = type;
    }

//    @SuppressWarnings("unchecked")
//    public ParametrizedType(ParameterizedType type) {
//        Objects.requireNonNull(type);
//        this.clazz = (Class<T>) type.getRawType();
//        this.type = type;
//    }

    @Override
    public Class<T> asClass() {
        return clazz;
    }

    @Override
    public Type asType() {
        return type;
    }

    @Override
    public ParameterizedType asParameterized() {
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
        var that = (ParametrizedType<?>) object;
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
