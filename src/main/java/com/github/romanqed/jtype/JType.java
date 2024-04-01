package com.github.romanqed.jtype;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

public interface JType<T> extends Type {

    Class<T> asClass();

    Type asType();

    default GenericArrayType asGenericArray() {
        return null;
    }

    default ParameterizedType asParameterized() {
        return null;
    }

    default WildcardType asWildcard() {
        return null;
    }

    @Override
    int hashCode();

    @Override
    boolean equals(Object o);

    @Override
    String toString();
}
