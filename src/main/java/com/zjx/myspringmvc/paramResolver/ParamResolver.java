package com.zjx.myspringmvc.paramResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 策略模式中的抽象的策略角色
 * 解析器的接口，不同的参数解析器需要实现它
 * Created by HP on 2019/7/6.
 */
public interface ParamResolver {
    /**
     * 判断当期前是否是某个类型
     *
     * @param type       类型
     * @param paramIndex 参数下标
     * @param method     需要的解析的方法
     * @return
     */
    boolean support(Class<?> type, int paramIndex, Method method);

    /**
     * 参数解析的方法
     * @param request
     * @param response
     * @param type 类型
     * @param paramIndex 参数下标
     * @param method 需要解析的方法
     * @return
     */
    Object paramResolver(HttpServletRequest request, HttpServletResponse response, Class<?> type, int paramIndex, Method method);
}
