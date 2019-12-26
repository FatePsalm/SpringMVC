package com.zjx.myspringmvc.annotaion;

import java.lang.annotation.*;

/**
 * 自定义@RequestMapping
 * Created by HP on 2019/7/5.
 */
@Target({ElementType.METHOD,ElementType.TYPE})//用于方法,类
@Retention(RetentionPolicy.RUNTIME)//可以通过反射得到
@Documented//该注解将被包含在javadoc中
public @interface MyRequestMapping {
    String value() default "";
}
