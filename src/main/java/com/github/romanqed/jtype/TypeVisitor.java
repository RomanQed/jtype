package com.github.romanqed.jtype;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.WildcardType;

public interface TypeVisitor {

    void visit(Class<?> type);

    void visit(WildcardType type);

    void visit(GenericArrayType type);

    void visit(ParameterizedType type);
}
