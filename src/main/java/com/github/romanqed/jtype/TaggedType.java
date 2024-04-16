package com.github.romanqed.jtype;

import java.lang.reflect.Type;

/**
 * An interface that adds tags to {@link Type}.
 */
public interface TaggedType extends Type {

    /**
     * Returns {@link Type} object without tags.
     * 
     * @return {@link Type} object without tags
     */
    Type getRawType();

    /**
     * Returns tags.
     * 
     * @return tags
     */
    Object[] getTags();
}
