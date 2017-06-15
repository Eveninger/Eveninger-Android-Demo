package com.evening.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Nighter on 17/6/14.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Factory {
    /**
     * 工厂的名字
     */
    Class type();

    /**
     * 表示生成那个对象的唯一id
     */
    String id();
}
