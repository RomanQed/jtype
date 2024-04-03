package com.github.romanqed.jtype;

import java.io.Serializable;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

final class GenericArrayTypeImpl implements GenericArrayType, Serializable {
    // UID
    private static final long serialVersionUID = 1786018160;

    private final Type component;

    GenericArrayTypeImpl(Type component) {
        this.component = component;
    }

    @Override
    public Type getGenericComponentType() {
        return component;
    }

    @Override
    public String getTypeName() {
        return TypeUtil.toString(component) + "[]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GenericArrayTypeImpl)) return false;
        var that = (GenericArrayTypeImpl) o;
        return component.equals(that.component);
    }

    @Override
    public int hashCode() {
        return component.hashCode();
    }

    @Override
    public String toString() {
        return TypeUtil.toString(component) + "[]";
    }
}
