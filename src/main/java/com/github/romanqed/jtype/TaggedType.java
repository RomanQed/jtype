package com.github.romanqed.jtype;

import java.lang.reflect.Type;

/**
 * An interface describing a {@link Type} modification
 * that has additional metadata in the form of tags.
 * A string representation of this type looks like this:
 * Type:Tag1:Tag2:Tag3...:TagN
 */
public interface TaggedType extends Type {

    /**
     * Returns the raw type without tag information.
     *
     * @return {@link Type} instance
     */
    Type getRawType();

    /**
     * Returns an array of tags.
     *
     * @return {@link Object} array
     */
    Object[] getTags();
}
