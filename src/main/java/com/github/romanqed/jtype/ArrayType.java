package com.github.romanqed.jtype;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

public final class ArrayType<T> implements JType<T> {
    private final Class<T> clazz;
    private final GenericArrayType type;

    ArrayType(Class<T> clazz, GenericArrayType type) {
        this.clazz = clazz;
        this.type = type;
    }

//    @SuppressWarnings("unchecked")
//    public ArrayType(GenericArrayType type) {
//        this.clazz = (Class<T>) TypeUtil.getRawType(type);
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
    public GenericArrayType asGenericArray() {
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
        var arrayType = (ArrayType<?>) object;
        return type.equals(arrayType.type);
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
