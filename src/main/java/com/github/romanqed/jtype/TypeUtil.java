package com.github.romanqed.jtype;

import java.lang.reflect.*;
import java.util.Objects;

public final class TypeUtil {
    private static final String ARRAY = "[";
    private static final String REFERENCE = "L";

    private TypeUtil() {
    }

    public static boolean isPrimitive(Type type) {
        return (type instanceof Class) && ((Class<?>) type).isPrimitive();
    }

    static String toString(Type type) {
        return type instanceof Class ? ((Class<?>) type).getName() : type.toString();
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
        Objects.requireNonNull(type);
        if (dimension < 1) {
            throw new IllegalArgumentException("Illegal array dimension: " + dimension);
        }
        return innerGetArrayType(type, dimension);
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
        Objects.requireNonNull(array);
        return innerGetRawType(array);
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
        if (type instanceof TaggedType) {
            return innerGetRawType(((TaggedType) type).getRawType());
        }
        if (type instanceof WildcardType) {
            var bounds = ((WildcardType) type).getUpperBounds();
            if (bounds.length != 1) {
                throw new IllegalTypeException("Multiple wildcards are not supported", type);
            }
            return innerGetRawType(bounds[0]);
        }
        throw new IllegalTypeException("Unexpected type implementation", type);
    }

    public static Class<?> getRawType(Type type) {
        Objects.requireNonNull(type);
        return innerGetRawType(type);
    }

    static Class<?> innerGetRawType(WildcardType type) {
        var bounds = type.getUpperBounds();
        if (bounds.length != 1) {
            throw new IllegalTypeException("Multiple wildcards are not supported", type);
        }
        return innerGetRawType(bounds[0]);
    }

    public static Class<?> getRawType(WildcardType type) {
        Objects.requireNonNull(type);
        return innerGetRawType(type);
    }
}
