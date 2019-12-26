package com.zjx.myspringmvc.annotaion;

import java.lang.annotation.*;

/**
 * 自定义@qualifier 可以用于指定要注入的bean
 * Created by HP on 2019/7/5.
 */
@Target({ElementType.FIELD})//用于字段、枚举的常量
@Retention(RetentionPolicy.RUNTIME)//可以通过反射得到
@Documented//该注解将被包含在javadoc中
public @interface MyQualifier {
    String value() default "";
}
