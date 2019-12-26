package com.zjx.myspringmvc.servelet;

import com.zjx.myspringmvc.annotaion.MyController;
import com.zjx.myspringmvc.annotaion.MyQualifier;
import com.zjx.myspringmvc.annotaion.MyRequestMapping;
import com.zjx.myspringmvc.annotaion.MyService;
import com.zjx.myspringmvc.controller.BaseController;
import com.zjx.myspringmvc.controller.UserController;
import com.zjx.myspringmvc.handlerAdapter.MyHandlerAdapterService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HP on 2019/7/5.
 */
public class DispatchServlet extends HttpServlet {
    //所有类名
    List<String> classNames = new ArrayList<String>();
    //IOC容器
    Map<String, Object> beans = new HashMap<String, Object>();
    //请求路径和执行方法的对应关系
    Map<String, Object> handlerMap = new HashMap<String, Object>();
    private static String HANDLERADAPTER = "myHandlerAdapterService";


    /**
     * 无参构造
     */
    public DispatchServlet() {

    }

    /**
     * 初始化方法
     *
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        //1、扫描哪些需要被实例化 class(包及包以下的class)
        scanPackage("com.zjx");
        for (String classname : classNames) {
            System.out.println(classname);
        }
        System.out.println("***********************");
        //2、扫描出来的类 进实例化
        instance();
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());

        }
        System.out.println("***********************");
        //依赖注入 这里项目简单 只有service层注入controller 后续读者可以自实现dao->service
        iocInject();

        // 4、建立一个path与method的映射关系
        HandlerMapping();
        for (Map.Entry<String, Object> entry : handlerMap.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());

        }
        System.out.println("***********************");

    }


    /**
     * 路径 与 方法
     */
    private void HandlerMapping() {
        if (beans.entrySet().size() <= 0) {
            System.out.println("没有类的实例化");
            return;
        }
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            Object instance = entry.getValue();

            Class<?> clazz = instance.getClass();
            //关注所有controller注解
            if (clazz.isAnnotationPresent(MyController.class)) {
                MyRequestMapping requestMapping = clazz.getAnnotation(MyRequestMapping.class);
                //获取请求路径
                String classPath = requestMapping.value();
                //获取controller里面的所有方法
                Method[] mehthods = clazz.getMethods();
                //再获取方法上的路径
                for (Method method : mehthods) {
                    if (method.isAnnotationPresent(MyRequestMapping.class)) {
                        MyRequestMapping req = method.getAnnotation(MyRequestMapping.class);
                        String methodPath = req.value();
                        handlerMap.put(classPath + methodPath, method);
                    }
                }
            }
        }
    }

    /**
     * 容器初始化 进行依赖注入
     */
    private void iocInject() {
        if (beans.entrySet().size() <= 0) {
            System.out.println("没有类的实例化");
            return;
        }
        //遍历实例化好的bean
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            Object instance = entry.getValue();//获得类的实例
            Class<?> clazz = instance.getClass();//获取类 用来获取注解信息
            if (clazz.isAnnotationPresent(MyController.class)) {
                Field[] fields = clazz.getDeclaredFields();//拿到类里面属性
                //判断类属性上是否有 自动装配（依赖注入）的注解 @Autowired或Qualifier
                for (Field field : fields) {
                    if (field.isAnnotationPresent(MyQualifier.class)) {
                        //获得注解的value 即容器中对应实例的key
                        MyQualifier qualifier = field.getAnnotation(MyQualifier.class);
                        String value = qualifier.value();
                        field.setAccessible(true);//因为类中属性private，获得许可

                        try {
                            //实例注入
                            field.set(instance, beans.get(value));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    } else {
                        continue;
                    }
                }
            } else {
                continue;
            }
        }
    }

    /**
     * 实例化，并加入到容器
     */
    private void instance() {
        if (classNames.size() <= 0) {
            System.out.println("包扫描失败！");
            return;
        }
        //遍历扫描到的class文件，将需要实例化的class利用反射创建对象，即加了注解的类，本项目为了简单
        //只需要关注@MyService 、@MyController的类注解，后续读者可以自己扩展。（ps:注解类不需要）
        for (String className : classNames) {

            //反射创建需要类名，扫描到的有.class后缀，去掉
            String cn = className.replace(".class", "");

            try {
                //得到class类
                Class<?> clazz = Class.forName(cn);
                //判断是否标记了注解 @MyController
                if (clazz.isAnnotationPresent(MyController.class)) {
                    MyController controller = clazz.getAnnotation(MyController.class);
                    Object instance = clazz.newInstance();//得到实例
                    //获取@MyRequestMapping注解的参数 即请求的路径“/user”作为key
                    MyRequestMapping requestMapping = clazz.getAnnotation(MyRequestMapping.class);
                    String key = requestMapping.value();
                    beans.put(key, instance);
                } else if (clazz.isAnnotationPresent(MyService.class)) {//同理 这里就是判断@Myservice注解
                    MyService service = clazz.getAnnotation(MyService.class);
                    Object instance = clazz.newInstance();
                    String key = service.value();
                    beans.put(key, instance);
                } else {
                    continue;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 包扫描
     *
     * @param basePackage 路径
     */
    private void scanPackage(String basePackage) {
        //扫描编译好的项目路径下所有的类 传进来的是com.zjx 要转换一下 /com/zjx
        URL url = this.getClass().getClassLoader().getResource("/" + replaceTo(basePackage));
        //得到路径
        String fileStr = url.getFile();
        File file = new File(fileStr);
        //拿到com.zjx 下的文件夹 myspringmvc
        String[] filesStr = file.list();
        for (String path : filesStr) {
            File filePath = new File(fileStr + path);

            //如果是文件夹继续扫描 ，递归调用
            if (filePath.isDirectory()) {
                //此时传进的参数就是com.zjx.myspringmvc 递归调用
                scanPackage(basePackage + "." + path);
            } else {
                classNames.add(basePackage + "." + filePath.getName());
            }
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取到请求路径 /mySpringMvc/user/query
        String uri = req.getRequestURI();
        //这里是个坑
        //浏览器会请求网站根目录的这个图标，如果网站根目录也没有这图标会产生 404
        //这里可以直接返回不做处理
        if (uri.equals("/favicon.ico")) {
            return;
        }
        //根据请求路径来获取要执行的方法
        Method method = (Method) handlerMap.get(uri);
        //拿到控制类
        BaseController instance = (BaseController) beans.get("/" + uri.split("/")[1]);
        System.out.println(instance + "*******");
        //接下就是 解析方法上的参数，以及调用方法，
        //一般简单的话我们就直接if else 判断参数的类型 注解等，但这样不太好 这里我们使用一个策略模式

        //拿到处理器
        MyHandlerAdapterService myHandlerAdapterService = (MyHandlerAdapterService) beans.get(HANDLERADAPTER);
        //得到参数
        Object[] args = myHandlerAdapterService.hand(req, resp, method, beans);

        //执行方法
        try {
            method.invoke(instance, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    private String replaceTo(String basePackage) {
        return basePackage.replaceAll("\\.", "/");
    }
}
