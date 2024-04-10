package com.github.romanqed.jtype;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Objects;

public final class Types {
    private static final Type[] EMPTY_ARRAY = new Type[0];
    private static final Type[] UPPERS = new Type[]{Object.class};

    private Types() {
    }

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

    public static GenericArrayType of(Type type) {
        Objects.requireNonNull(type);
        return new GenericArrayTypeImpl(type);
    }

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

    public static ParameterizedType ofOwned(Type owner, Type raw, Type... arguments) {
        Objects.requireNonNull(raw);
        return new ParameterizedTypeImpl(owner, raw, arguments.clone());
    }

    public static ParameterizedType of(Type raw, Type... arguments) {
        Objects.requireNonNull(raw);
        return new ParameterizedTypeImpl(null, raw, arguments.clone());
    }

    public static WildcardType subtypeOf(Type type) {
        if (type instanceof WildcardType) {
            return new WildcardTypeImpl(((WildcardType) type).getUpperBounds(), EMPTY_ARRAY);
        }
        return new WildcardTypeImpl(new Type[]{type}, EMPTY_ARRAY);
    }

    public static WildcardType supertypeOf(Type type) {
        if (type instanceof WildcardType) {
            return new WildcardTypeImpl(UPPERS, ((WildcardType) type).getLowerBounds());
        }
        return new WildcardTypeImpl(UPPERS, new Type[]{type});
    }

    public static TaggedType of(Type type, Object[] tags) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(tags);
        return new TaggedTypeImpl(type, tags.clone());
    }
}
