package com.github.aclijpio.bloghub;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GetClassTypeFromGenericTest {


    @Test
    public void getClassTypeFromGeneric() {

        TestObject<User> user = new TestObject<>() {};
        Assertions.assertEquals(user.getInnerClassType().getName(), User.class.getName());
    }

    public static class User {
        private String id;
        private String name;
    }
    public static abstract class TestObject<T> {
        @SuppressWarnings("unchecked")
        final public Class<T> getInnerClassType(){
            Type t = getClass().getGenericSuperclass();
            ParameterizedType pt = (ParameterizedType) t;
            return (Class<T>) pt.getActualTypeArguments()[0];
        }
    }

}
