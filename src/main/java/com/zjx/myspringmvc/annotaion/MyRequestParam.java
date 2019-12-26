package com.zjx.myspringmvc.annotaion;

import java.lang.annotation.*;

/**
 * 自定义@RequestParam
 * Created by HP on 2019/7/5.
 */
@Target({ElementType.PARAMETER})//用于方法参数
@Retention(RetentionPolicy.RUNTIME)//可以通过反射得到
@Documented//该注解将被包含在javadoc中
public @interface MyRequestParam {
    String value() default "";
}
