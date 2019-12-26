package com.zjx.myspringmvc.controller;

import com.zjx.myspringmvc.annotaion.MyController;
import com.zjx.myspringmvc.annotaion.MyQualifier;
import com.zjx.myspringmvc.annotaion.MyRequestMapping;
import com.zjx.myspringmvc.annotaion.MyRequestParam;
import com.zjx.myspringmvc.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by HP on 2019/7/5.
 */
@MyController
@MyRequestMapping("/user")
public class UserController implements  BaseController{

    @MyQualifier("UserServiceImpl")
    private UserService userService;
    @MyRequestMapping("/query")
    public void query(HttpServletRequest request, HttpServletResponse response, @MyRequestParam("name") String name, @MyRequestParam("age") String age) {
        PrintWriter pw;
        try {
            pw=response.getWriter();
            String result=userService.query(name, age);
            pw.write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
