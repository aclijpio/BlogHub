package com.github.aclijpio.bloghub;

import com.github.aclijpio.bloghub.database.annotaions.Column;
import com.github.aclijpio.bloghub.database.annotaions.Entity;
import com.github.aclijpio.bloghub.database.annotaions.Id;
import com.github.aclijpio.bloghub.database.repository.SimpleCrudRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ClassTypeFromGenericTest {


    @Test
    public void getClassTypeFromGeneric() {

        TestObject<User> user = new TestObject<>() {};
        Assertions.assertEquals(User.class.getName(), user.getInnerClassType().getName());
    }


    @Test
    public void getInnerClassTypeFromSimpleCrudRepository() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        SimpleCrudRepository<User, String> repository = new SimpleCrudRepository<>() {};

        Method method = SimpleCrudRepository.class.getDeclaredMethod("getInnerClassType");
        method.setAccessible(true);

        Class<?> innerClassType = (Class<?>) method.invoke(repository);
        Assertions.assertNotNull(innerClassType);

        Assertions.assertEquals(User.class, innerClassType);
    }

    @Entity("users")
    public static class User {
        @Id
        private String id;
        @Column
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
