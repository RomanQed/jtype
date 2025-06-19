package com.github.romanqed.jtype;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * A class representing a universal type descriptor containing information
 * about the raw class and its specialized generics.
 *
 * @param <T> a stored type
 */
public class JType<T> {
    public static final JType<Boolean> BOOLEAN = new JType<>(Boolean.class);
    public static final JType<Character> CHARACTER = new JType<>(Character.class);
    public static final JType<Byte> BYTE = new JType<>(Byte.class);
    public static final JType<Short> SHORT = new JType<>(Short.class);
    public static final JType<Integer> INTEGER = new JType<>(Integer.class);
    public static final JType<Long> LONG = new JType<>(Long.class);
    public static final JType<Float> FLOAT = new JType<>(Float.class);
    public static final JType<Double> DOUBLE = new JType<>(Double.class);

    private final Class<T> raw;
    private final Type type;

    JType(Class<T> raw, Type type) {
        this.raw = raw;
        this.type = type;
    }

    JType(Class<T> raw) {
        this.raw = raw;
        this.type = raw;
    }

    /**
     * Constructs a JType instance based on the type extracted from
     * the type argument specified during inheritance.
     * This constructor can be used in one of the following two ways.
     * The first, recommended:
     * <pre>
     *     var type = new JType&lt;List&lt;String&gt;&gt;() {};
     * </pre>
     * And another one, possible, but not recommended:
     * <pre>
     *     class MyJTypeImpl extends JType&lt;List&lt;String&gt;&gt; {}
     *     ...
     *     var type = new MyJTypeImpl();
     * </pre>
     * IMPORTANT! Make sure that the type argument is passed by the first inheriting class
     * and your jar package builder does not delete metadata containing generic information.
     */
    @SuppressWarnings("unchecked")
    protected JType() {
        this.type = getTypeArgument();
        this.raw = (Class<T>) TypeUtil.innerGetRawType(this.type);
    }

    /**
     * Creates a JType instance containing the type of the specified class.
     *
     * @param type the specified {@link Class} instance, must be non-null
     * @param <T>  the type of the specified class
     * @return {@link JType} instance
     */
    public static <T> JType<T> of(Class<T> type) {
        Objects.requireNonNull(type);
        return new JType<>(type);
    }

    /**
     * Creates a JType instance containing the specified type and raw class,
     * extracted from it.
     *
     * @param type the specified {@link Type} instance, must be non-null
     * @param <T>  the specified type
     * @return {@link JType} instance
     */
    @SuppressWarnings("unchecked")
    public static <T> JType<T> of(Type type) {
        Objects.requireNonNull(type);
        var raw = (Class<T>) TypeUtil.innerGetRawType(type);
        return new JType<>(raw, type);
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

    /**
     * Returns {@link Class} instance, containing raw type.
     *
     * @return {@link Class} instance
     */
    public Class<T> getRawType() {
        return raw;
    }

    /**
     * Returns {@link Type} instance, containing full type.
     *
     * @return {@link Type} instance
     */
    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
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
