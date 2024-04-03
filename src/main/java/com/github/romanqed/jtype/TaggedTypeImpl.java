package com.github.romanqed.jtype;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Arrays;

final class TaggedTypeImpl implements TaggedType, Serializable {
    // UID
    private static final long serialVersionUID = 1786018160;

    private final Type raw;
    private final String[] tags;

    TaggedTypeImpl(Type raw, String[] tags) {
        this.raw = raw;
        this.tags = tags;
    }

    @Override
    public Type getRawType() {
        return raw;
    }

    @Override
    public String[] getTags() {
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
        if (!(o instanceof TaggedTypeImpl)) return false;
        var that = (TaggedTypeImpl) o;
        return raw.equals(that.raw) && Arrays.equals(tags, that.tags);
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
