package com.github.romanqed.jtype;

import java.lang.reflect.Type;

public interface TaggedType extends Type {

    Type getRawType();

    String[] getTags();
}
