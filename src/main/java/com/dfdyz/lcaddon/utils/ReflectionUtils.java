package com.dfdyz.lcaddon.utils;

import java.lang.reflect.Field;

public class ReflectionUtils {

    public static <T> T GetField(Class clazz, String fieldname){
        T val = null;
        try{
            Field field = clazz.getDeclaredField(fieldname);
            field.setAccessible(true);
            val = (T)field.get(clazz);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return val;
    }
}
