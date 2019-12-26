package com.zjx.myspringmvc.paramResolver;

import com.zjx.myspringmvc.annotaion.MyRequestParam;
import com.zjx.myspringmvc.annotaion.MyService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 具体策略角色
 *解析声明注解为RequestParam, 获取注解的值
 * Created by HP on 2019/7/6.
 */
@MyService("myRequestParamResolver")
public class MyRequestParamResolver implements ParamResolver {
    //判断传进来的参数是否为EnjoyRequestParam
    public boolean support(Class<?> type, int paramIndex, Method method) {
        //query(HttpServletRequest request, HttpServletResponse response,
        // @MyRequestParam("name") String name, @MyRequestParam("age") int age)
        //这里拿到一个二维数组
        Annotation[][] an=method.getParameterAnnotations();
        Annotation[] paramAns=an[paramIndex];
        for (Annotation paramAn:paramAns){
            //传进来的paramAn.getClass()是不是 EnjoyRequestParam 类型
            //解释一下a.isAssignableFrom(b)
            //a对象所对应类信息是b对象所对应的类信息的父类或者是父接口，简单理解即a是b的父类或接口
            //a对象所对应类信息与b对象所对应的类信息相同，简单理解即a和b为同一个类或同一个接口
            if (MyRequestParam.class.isAssignableFrom(paramAn.getClass())){
                return true;
            }
        }
        return false;
    }
    //参数解析,并获取注解的值
    public Object paramResolver(HttpServletRequest request, HttpServletResponse response, Class<?> type, int paramIndex, Method method) {
        Annotation[][] an=method.getParameterAnnotations();
        Annotation[] paramAns=an[paramIndex];
        for (Annotation paramAn:paramAns){
            if (MyRequestParam.class.isAssignableFrom(paramAn.getClass())){
                MyRequestParam myRequestParam= (MyRequestParam) paramAn;
                String value=myRequestParam.value();
                System.out.println("浏览器参数值"+value+":::"+request.getParameter(value));
                //根据注解的值 从请求中获取值
                return  request.getParameter(value);
            }
        }
        return  null;

    }
}
