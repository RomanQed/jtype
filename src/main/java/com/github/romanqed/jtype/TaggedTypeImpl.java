package com.github.romanqed.jtype;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Arrays;

final class TaggedTypeImpl implements TaggedType, Serializable {
    // UID
    private static final long serialVersionUID = 1786018160;

    private final Type raw;
    private final Object[] tags;

    TaggedTypeImpl(Type raw, Object[] tags) {
        this.raw = raw;
        this.tags = tags;
    }

    @Override
    public Type getRawType() {
        return raw;
    }

    @Override
    public Object[] getTags() {
        return tags.clone();
    }

    @Override
    public String getTypeName() {
        var builder = new StringBuilder(TypeUtil.toString(raw));
        for (var tag : tags) {
            builder.append(':').append(tag);
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaggedType)) return false;
        var that = (TaggedType) o;
        return raw.equals(that.getRawType()) && Arrays.equals(tags, that.getTags());
    }

    @Override
    public int hashCode() {
        return 31 * raw.hashCode() + Arrays.hashCode(tags);
    }

    @Override
    public String toString() {
        return getTypeName();
    }
}
