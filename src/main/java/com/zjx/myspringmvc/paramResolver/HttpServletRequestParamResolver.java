package com.zjx.myspringmvc.paramResolver;

import com.zjx.myspringmvc.annotaion.MyService;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 具体的策略角色
 * HttpServletRequest解析
 * Created by HP on 2019/7/6.
 */
@MyService("httpServletRequestParamResolver")
public class HttpServletRequestParamResolver implements ParamResolver {
    public boolean support(Class<?> type, int paramIndex, Method method) {
        return ServletRequest.class.isAssignableFrom(type);
    }

    public Object paramResolver(HttpServletRequest request, HttpServletResponse response, Class<?> type, int paramIndex, Method method) {
        return request;
    }
}
