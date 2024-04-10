package com.github.romanqed.jtype;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

final class ParameterizedTypeImpl implements ParameterizedType, Serializable {
    // UID
    private static final long serialVersionUID = 1786018160;

    private final Type owner;
    private final Type raw;
    private final Type[] arguments;

    ParameterizedTypeImpl(Type owner, Type raw, Type[] arguments) {
        this.owner = owner;
        this.raw = raw;
        this.arguments = arguments;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return arguments;
    }

    @Override
    public Type getRawType() {
        return raw;
    }

    @Override
    public Type getOwnerType() {
        return owner;
    }

    @Override
    public String getTypeName() {
        // Thanks to Bob Lee, Jesse Wilson and their code in the gson project
        var length = arguments.length;
        if (length == 0) {
            return TypeUtil.toString(raw);
        }
        // Simple optimization to avoid unnecessary buffer re-allocations
        // The heuristic is to consider the length of each type name to be 30 characters
        var builder = new StringBuilder(30 * (length + 1));
        // Type<First
        builder
                .append(TypeUtil.toString(raw))
                .append("<")
                .append(TypeUtil.toString(arguments[0]));
        // Type<First, Second, Third, ...
        for (var i = 1; i < length; ++i) {
            builder.append(", ").append(TypeUtil.toString(arguments[i]));
        }
        return builder.append(">").toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParameterizedType)) return false;
        var that = (ParameterizedType) o;
        return Objects.equals(owner, that.getOwnerType())
                && raw.equals(that.getRawType())
                && Arrays.equals(arguments, that.getActualTypeArguments());
    }

    @Override
    public int hashCode() {
        // Hash from openjdk
        return Arrays.hashCode(arguments)
                ^ (owner == null ? 0 : owner.hashCode())
                ^ raw.hashCode();
    }

    @Override
    public String toString() {
        return getTypeName();
    }
}
