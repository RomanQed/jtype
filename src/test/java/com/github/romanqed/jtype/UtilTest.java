package com.github.romanqed.jtype;

import org.junit.jupiter.api.Test;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.List;

import static com.github.romanqed.jtype.TypeUtil.*;
import static org.junit.jupiter.api.Assertions.*;

public class UtilTest {

    @Test
    public void testPrimitiveChecker() {
        assertAll(
                () -> assertTrue(isPrimitive(boolean.class)),
                () -> assertTrue(isPrimitive(char.class)),
                () -> assertTrue(isPrimitive(byte.class)),
                () -> assertTrue(isPrimitive(short.class)),
                () -> assertTrue(isPrimitive(int.class)),
                () -> assertTrue(isPrimitive(long.class)),
                () -> assertTrue(isPrimitive(float.class)),
                () -> assertTrue(isPrimitive(double.class)),
                () -> assertTrue(isPrimitive(void.class))
        );
    }

    @Test
    public void testGetArrayType() {
        assertAll(
                () -> assertThrows(NullPointerException.class, () -> getArrayType(null, 1)),
                () -> assertThrows(IllegalArgumentException.class, () -> getArrayType(String.class, 0)),
                () -> assertEquals(String[].class, getArrayType(String.class, 1)),
                () -> assertEquals(String[][].class, getArrayType(String[].class, 1))
        );
    }

    @Test
    public void testGetRawTypeFromArray() {
        var type = Types.of(Types.of(List.class, String.class), 1);
        assertAll(
                () -> assertThrows(NullPointerException.class, () -> getRawType((GenericArrayType) null)),
                () -> assertEquals(List[].class, getRawType(type))
        );
    }

    @Test
    public void testGetRawWildcardType() {
        var w1 = Types.subtypeOf(List.class);
        var w2 = Types.supertypeOf(Number.class);
        assertAll(
                () -> assertThrows(NullPointerException.class, () -> getRawType((WildcardType) null)),
                () -> assertEquals(List.class, getRawType(w1)),
                () -> assertEquals(Object.class, getRawType(w2))
        );
    }

    @Test
    public void testGetRawType() {
        // java.util.List<? extends java.lang.String>[][][]
        var array = Types.of(Types.of(List.class, Types.subtypeOf(String.class)), 3);
        // ? extends java.util.List<? extends java.lang.String>[][][]
        var wildcard = Types.subtypeOf(array);
        // java.util.List<? extends java.util.List<? extends java.lang.String>[][][]>
        var type = Types.of(List.class, wildcard);
        assertAll(
                () -> assertThrows(NullPointerException.class, () -> getRawType((Type) null)),
                () -> assertEquals(String.class, getRawType(String.class)),
                () -> assertEquals(List[][][].class, getRawType(array)),
                () -> assertEquals(List[][][].class, getRawType(wildcard)),
                () -> assertEquals(List.class, getRawType(type))
        );
    }
}
