package com.zjx.myspringmvc.handlerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by HP on 2019/7/6.
 */
public interface MyHandlerAdapterService {
  Object[] hand(HttpServletRequest request,//拿request请求里的参数
                         HttpServletResponse response,//
                         Method method,
                         Map<String, Object> beans);
}
