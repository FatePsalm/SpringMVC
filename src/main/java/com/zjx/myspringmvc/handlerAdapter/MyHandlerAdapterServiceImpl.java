package com.zjx.myspringmvc.handlerAdapter;

import com.zjx.myspringmvc.annotaion.MyService;
import com.zjx.myspringmvc.paramResolver.ParamResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by HP on 2019/7/6.
 */
@MyService("myHandlerAdapterService")
public class MyHandlerAdapterServiceImpl implements MyHandlerAdapterService {
    /**
     * //对method方法里的参数进行处理
     * @param request 拿请求的参数
     * @param response
     * @param method 可以拿到当前待执行的方法有哪些参数
     * @param beans ioc容器
     * @return
     */
    public Object[] hand(HttpServletRequest request, HttpServletResponse response, Method method, Map<String, Object> beans) {
        //拿到当前待执行的方法有哪些参数
        Class<?>[] paramClazzs=method.getParameterTypes();
        Object[] args=new Object[paramClazzs.length];
        //拿到所有实现了ParamResolver这个接口的实现类
        Map<String,Object> resolvers=getBeansOfType(beans, ParamResolver.class);
        int paramIndex=0;
        int i=0;
        //遍历所有参数
        for (Class<?> paramClazz:paramClazzs){
            System.out.println("当前参数的类型："+paramClazz);
            //遍历所有解析器
            for (Map.Entry<String,Object> entry:resolvers.entrySet()){
                ParamResolver pr= (ParamResolver) entry.getValue();
                //是否应用当前解析器
                if (pr.support(paramClazz,paramIndex,method)){
                    //获得参数值
                    args[i++]=pr.paramResolver(request,response,paramClazz,paramIndex,method);
                }
            }

            paramIndex++;
        }
        return args;
    }

    /**
     * 获取所有解析器的实例 即实现了ParamResolver接口的实例
     * @param beans IOC容器
     * @param intfType 接口类型
     * @return
     */
    private Map<String,Object> getBeansOfType(Map<String,Object> beans,Class<?> intfType){
        Map<String,Object> res=new HashMap<String, Object>();
        for (Map.Entry<String,Object> bean:beans.entrySet()){
         //拿到实例-->反射对象-->它的接口(接口有多实现,所以为数组)
            Class<?>[]  clazzs=bean.getValue().getClass().getInterfaces();
            if (clazzs!=null && clazzs.length>0){
                for (Class<?> clazz:clazzs){
                    //实现了对应接口，放到结果集里面
                    if (clazz.isAssignableFrom(intfType)){
                        res.put(bean.getKey(),bean.getValue());
                    }
                }
            }
        }
        return  res;
    }
}
