package com.github.romanqed.jtype;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ImplsTest {

    @Test
    public void testParameterized() throws NoSuchFieldException {
        var expected = A.class.getDeclaredField("a").getGenericType();
        var actual = Types.of(List.class, String.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testGenericArray() throws NoSuchFieldException {
        var expected = A.class.getDeclaredField("b").getGenericType();
        var actual = Types.of(Types.of(List.class, String.class));
        assertEquals(expected, actual);
    }

    @Test
    public void testSubtype() throws NoSuchFieldException {
        var expected = A.class.getDeclaredField("c").getGenericType();
        var actual = Types.of(List.class, Types.subtypeOf(String.class));
        assertEquals(expected, actual);
    }

    @Test
    public void testSupertype() throws NoSuchFieldException {
        var expected = A.class.getDeclaredField("d").getGenericType();
        var actual = Types.of(List.class, Types.supertypeOf(Number.class));
        assertEquals(expected, actual);
    }

    public static final class A {
        List<String> a;
        List<String>[] b;
        List<? extends String> c;
        List<? super Number> d;
    }
}
