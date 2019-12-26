package com.zjx.myspringmvc.annotaion;

import java.lang.annotation.*;

/**
 * 自定义@Service
 * Created by HP on 2019/7/5.
 */
@Target({ElementType.TYPE})//可以用在接口、类、枚举
@Retention(RetentionPolicy.RUNTIME)//可以通过反射得到
@Documented//该注解将被包含在javadoc中
public @interface MyService {
    String value() default "";
}
