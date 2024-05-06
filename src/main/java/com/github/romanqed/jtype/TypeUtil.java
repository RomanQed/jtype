package com.github.romanqed.jtype;

import java.lang.reflect.*;
import java.util.Objects;

/**
 * A utility class containing methods for extracting raw types and some others.
 * This class cannot be instantiated.
 */
public final class TypeUtil {
    private static final String ARRAY = "[";
    private static final String REFERENCE = "L";

    private TypeUtil() {
    }

    /**
     * Checks whether the specified type is a primitive.
     *
     * @param type the specified type, may be null
     * @return true if the type is a primitive, false otherwise
     */
    public static boolean isPrimitive(Type type) {
        return (type instanceof Class) && ((Class<?>) type).isPrimitive();
    }

    static String toString(Type type) {
        return type instanceof Class ? ((Class<?>) type).getName() : type.toString();
    }

    static Class<?> innerGetArrayType(Class<?> type, int dimension) {
        try {
            if (type.isArray()) {
                return Class.forName(
                        ARRAY.repeat(dimension) + type.getName(),
                        false,
                        type.getClassLoader()
                );
            }
            return Class.forName(
                    ARRAY.repeat(dimension) + REFERENCE + type.getName() + ";",
                    false,
                    type.getClassLoader()
            );
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates an array type from the specified type and dimension.
     * In fact, the dimension will simply be added to the type descriptor,
     * meaning the method works as follows:
     * <br>
     * 1) getArrayType(String.class, 1) =&gt; String[].class
     * <br>
     * 2) getArrayType(String[].class, 1) =&gt; String[][].class
     *
     * @param type      the specified component type, must be non-null
     * @param dimension the specified dimension, must be greater or equal 1
     * @return {@link Class} instance
     */
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

    /**
     * Extracts raw type from the specified {@link GenericArrayType} instance.
     *
     * @param array the specified {@link GenericArrayType} instance, must be non-null
     * @return {@link Class} instance, containing raw type
     */
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

    /**
     * Extracts raw type from the specified {@link Type} instance.
     *
     * @param type the specified {@link Type} instance, must be non-null
     * @return {@link Class} instance, containing raw type
     */
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

    /**
     * Extracts raw type from the specified {@link WildcardType} instance.
     *
     * @param type the specified {@link WildcardType} instance, must be non-null
     * @return {@link Class} instance, containing raw type
     */
    public static Class<?> getRawType(WildcardType type) {
        Objects.requireNonNull(type);
        return innerGetRawType(type);
    }
}
