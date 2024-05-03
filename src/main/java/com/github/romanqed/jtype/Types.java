package com.github.romanqed.jtype;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Objects;

/**
 * A utility class containing constructor methods that allow you to create implementations of basic type interfaces:
 * {@link GenericArrayType}, {@link ParameterizedType}, {@link WildcardType} and {@link TaggedType}.
 * This class cannot be instantiated.
 */
public final class Types {
    private static final Type[] EMPTY_ARRAY = new Type[0];
    private static final Type[] UPPERS = new Type[]{Object.class};

    private Types() {
    }

    /**
     * Re-instantiates the passed type with jtype implementations
     * to maintain the consistency of the equals and hashCode methods.
     *
     * @param type the specified type, may be null
     * @return {@link Type} instance or null
     */
    public static Type canonicalize(Type type) {
        if (type == null) {
            return null;
        }
        if (type instanceof GenericArrayType) {
            var component = ((GenericArrayType) type).getGenericComponentType();
            return new GenericArrayTypeImpl(canonicalize(component));
        }
        if (type instanceof ParameterizedType) {
            var parameterized = (ParameterizedType) type;
            var owner = canonicalize(parameterized.getOwnerType());
            var raw = canonicalize(parameterized.getRawType());
            var old = parameterized.getActualTypeArguments();
            var length = old.length;
            var types = new Type[length];
            for (var i = 0; i < length; ++i) {
                types[i] = canonicalize(old[i]);
            }
            return new ParameterizedTypeImpl(
                    owner,
                    raw,
                    types
            );
        }
        if (type instanceof WildcardType) {
            var wildcard = (WildcardType) type;
            var oldUppers = wildcard.getUpperBounds();
            var length = oldUppers.length;
            var uppers = new Type[length];
            for (var i = 0; i < length; ++i) {
                uppers[i] = canonicalize(oldUppers[i]);
            }
            var oldLowers = wildcard.getLowerBounds();
            length = oldLowers.length;
            var lowers = new Type[length];
            for (var i = 0; i < length; ++i) {
                lowers[i] = canonicalize(oldLowers[i]);
            }
            return new WildcardTypeImpl(uppers, lowers);
        }
        if (type instanceof TaggedType) {
            var tagged = (TaggedType) type;
            return new TaggedTypeImpl(canonicalize(tagged.getRawType()), tagged.getTags());
        }
        return type;
    }

    /**
     * Creates {@link GenericArrayType} instance with the specified component type.
     *
     * @param type the specified component type, must be non-null
     * @return {@link GenericArrayType} instance
     */
    public static GenericArrayType of(Type type) {
        Objects.requireNonNull(type);
        return new GenericArrayTypeImpl(type);
    }

    /**
     * Creates {@link GenericArrayType} instance with the specified component type and dimension.
     *
     * @param type      the specified component type, must be non-null
     * @param dimension the specified dimension, must be greater or equal than 1
     * @return {@link GenericArrayType} instance
     */
    public static GenericArrayType of(Type type, int dimension) {
        Objects.requireNonNull(type);
        if (dimension < 1) {
            throw new IllegalArgumentException("Illegal dimension: " + dimension);
        }
        if (dimension == 1) {
            return new GenericArrayTypeImpl(type);
        }
        var ret = new GenericArrayTypeImpl(type);
        while (--dimension > 0) {
            ret = new GenericArrayTypeImpl(ret);
        }
        return ret;
    }

    /**
     * Creates {@link ParameterizedType} instance with the specified owner, raw and argument types.
     *
     * @param owner     the specified owner type, may be null
     * @param raw       the specified raw type, must be non-null
     * @param arguments the specified type arguments
     * @return {@link ParameterizedType} instance
     */
    public static ParameterizedType ofOwned(Type owner, Type raw, Type... arguments) {
        Objects.requireNonNull(raw);
        return new ParameterizedTypeImpl(owner, raw, arguments.clone());
    }

    /**
     * Creates {@link ParameterizedType} instance with the specified raw and argument types.
     *
     * @param raw       the specified raw type, must be non-null
     * @param arguments the specified type arguments
     * @return {@link ParameterizedType} instance
     */
    public static ParameterizedType of(Type raw, Type... arguments) {
        Objects.requireNonNull(raw);
        if (raw instanceof Class) {
            var clazz = (Class<?>) raw;
            return new ParameterizedTypeImpl(clazz.getEnclosingClass(), clazz, arguments.clone());
        }
        return new ParameterizedTypeImpl(null, raw, arguments.clone());
    }

    /**
     * Creates {@link ParameterizedType} instance with the specified raw and argument types.
     *
     * @param raw       the specified raw type, must be non-null
     * @param arguments the specified type arguments
     * @return {@link ParameterizedType} instance
     */
    public static ParameterizedType of(Class<?> raw, Type... arguments) {
        Objects.requireNonNull(raw);
        return new ParameterizedTypeImpl(raw.getEnclosingClass(), raw, arguments.clone());
    }

    /**
     * Creates {@link WildcardType} instance with the specified subtype.
     *
     * @param type the specified subtype, must be non-null
     * @return {@link WildcardType} instance
     */
    public static WildcardType subtypeOf(Type type) {
        Objects.requireNonNull(type);
        if (type instanceof WildcardType) {
            return new WildcardTypeImpl(((WildcardType) type).getUpperBounds(), EMPTY_ARRAY);
        }
        return new WildcardTypeImpl(new Type[]{type}, EMPTY_ARRAY);
    }

    /**
     * Creates {@link WildcardType} instance with the specified supertype.
     *
     * @param type the specified supertype, must be non-null
     * @return {@link WildcardType} instance
     */
    public static WildcardType supertypeOf(Type type) {
        Objects.requireNonNull(type);
        if (type instanceof WildcardType) {
            return new WildcardTypeImpl(UPPERS, ((WildcardType) type).getLowerBounds());
        }
        return new WildcardTypeImpl(UPPERS, new Type[]{type});
    }

    /**
     * Creates {@link TaggedType} instance with the specified raw type and tags.
     *
     * @param type the specified raw type, must be non-null
     * @param tags the specified tags, must be non-null
     * @return {@link TaggedType} instance
     */
    public static TaggedType of(Type type, Object[] tags) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(tags);
        return new TaggedTypeImpl(type, tags.clone());
    }
}
