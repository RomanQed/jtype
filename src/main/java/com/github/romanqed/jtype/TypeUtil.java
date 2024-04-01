package com.github.romanqed.jtype;

import java.lang.reflect.*;

public final class TypeUtil {
    private static final String ARRAY = "[";
    private static final String REFERENCE = "L";

    private TypeUtil() {
    }

    static Class<?> innerGetArrayType(Class<?> type, int dimension) {
        try {
            return Class.forName(
                    ARRAY.repeat(dimension) + REFERENCE + type.getTypeName() + ";",
                    false,
                    type.getClassLoader()
            );
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Class<?> getArrayType(Class<?> type, int dimension) {
        return null;
    }

    static Class<?> innerGetRawType(GenericArrayType type) {
        var temp = (Type) type;
        var count = 0;
        while (temp instanceof GenericArrayType) {
            temp = ((GenericArrayType) temp).getGenericComponentType();
            ++count;
        }
        if (temp instanceof TypeVariable) {
            return innerGetArrayType(Object.class, count);
        }
        var parameterized = (ParameterizedType) temp;
        var clazz = (Class<?>) parameterized.getRawType();
        return innerGetArrayType(clazz, count);
    }

    public static Class<?> getRawType(GenericArrayType array) {
        return null;
    }

    static Class<?> innerGetRawType(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        }
        if (type instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) type).getRawType();

        }
        if (type instanceof GenericArrayType) {
            return innerGetRawType((GenericArrayType) type);
        }
        if (type instanceof TypeVariable) {
            // We cannot statically derive a generic, so we use type erasure.
            return Object.class;
        }
        if (type instanceof WildcardType) {
            var bounds = ((WildcardType) type).getUpperBounds();
            if (bounds.length != 1) {
                throw new IllegalTypeException("Multiple wildcards are not supported", type);
            }
            return innerGetRawType(bounds[0]);
        }
        throw new IllegalArgumentException("Unexpected Type implementation: " + type.getClass());
    }

    public static Class<?> getRawType(Type type) {
        return null;
    }

    static Class<?> innerGetRawType(WildcardType type) {
        var bounds = type.getUpperBounds();
        if (bounds.length != 1) {
            throw new IllegalTypeException("Multiple wildcards are not supported", type);
        }
        return innerGetRawType(bounds[0]);
    }

    public static Class<?> getRawType(WildcardType type) {
        return null;
    }
}
