package com.github.romanqed.jtype;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Objects;

/**
 * A class that contains static methods to create objects from this package.
 * This class object cannot be instantiated.
 */
public final class Types {
    private static final Type[] EMPTY_ARRAY = new Type[0];
    private static final Type[] UPPERS = new Type[]{Object.class};

    private Types() {
    }

    /**
     * Creates an similar {@link Type} object of the same interface but using
     * own implementation.
     * 
     * @param type
     * @return copy of {@link Type} object
     */
    public static Type canonicalize(Type type) {
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
     * Creates {@link GenericArrayType} implementation instance with the 
     * {@link Type} object.
     * 
     * @param type
     * @return {@link GenericArrayType} implementation object
     */
    public static GenericArrayType of(Type type) {
        Objects.requireNonNull(type);
        return new GenericArrayTypeImpl(type);
    }

    /**
     * Creates {@link GenericArrayType} implementation instance with the 
     * {@link Type} object and dimensions count.
     * 
     * @param type
     * @param dimension
     * @return {@link GenericArrayType} implementation object
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
     * Creates {@link ParameterizedType} implementation instance with the
     * class owner, raw class and class arguments.
     * 
     * @param owner
     * @param raw
     * @param arguments
     * @return {@link ParameterizedType} implementation object
     */
    public static ParameterizedType ofOwned(Type owner, Type raw, Type... arguments) {
        Objects.requireNonNull(raw);
        return new ParameterizedTypeImpl(owner, raw, arguments.clone());
    }

    /**
     * Creates {@link ParameterizedType} implementation instance with the
     * raw class and class arguments.
     * 
     * @param raw
     * @param arguments
     * @return {@link ParameterizedType} implementation object
     */
    public static ParameterizedType of(Type raw, Type... arguments) {
        Objects.requireNonNull(raw);
        return new ParameterizedTypeImpl(null, raw, arguments.clone());
    }

    /**
     * Creates {@link WildcardType} implementation instance with the
     * {@link Type} object.
     * 
     * @param type
     * @return Creates {@link WildcardType} implementation object that contains
     * {@link Type} object subtypes.
     */
    public static WildcardType subtypeOf(Type type) {
        Objects.requireNonNull(type);
        if (type instanceof WildcardType) {
            return new WildcardTypeImpl(((WildcardType) type).getUpperBounds(), EMPTY_ARRAY);
        }
        return new WildcardTypeImpl(new Type[]{type}, EMPTY_ARRAY);
    }

    /**
     * Creates {@link WildcardType} implementation instance with the
     * {@link Type} object.
     * 
     * @param type
     * @return Creates {@link WildcardType} implementationobject that contains
     * {@link Type} object supertypes.
     */
    public static WildcardType supertypeOf(Type type) {
        Objects.requireNonNull(type);
        if (type instanceof WildcardType) {
            return new WildcardTypeImpl(UPPERS, ((WildcardType) type).getLowerBounds());
        }
        return new WildcardTypeImpl(UPPERS, new Type[]{type});
    }

    /**
     * Creates {@link TaggedType} implementation instance with the
     * {@link Type} object and tags array.
     * 
     * @param type
     * @param tags
     * @return {@link TaggedType} implementation object
     */
    public static TaggedType of(Type type, Object[] tags) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(tags);
        return new TaggedTypeImpl(type, tags.clone());
    }
}
