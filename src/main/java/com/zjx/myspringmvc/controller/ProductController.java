package com.zjx.myspringmvc.controller;

import com.zjx.myspringmvc.annotaion.MyController;
import com.zjx.myspringmvc.annotaion.MyRequestMapping;
import com.zjx.myspringmvc.annotaion.MyRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * Created by HP on 2019/7/5.
 */
@MyController
@MyRequestMapping("/product")
public class ProductController implements  BaseController{
    @MyRequestMapping("/query")
    public void query(HttpServletRequest request, HttpServletResponse response, @MyRequestParam("productId") String productId) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter pw;
        try {
            pw=response.getWriter();
            pw.write("商品："+productId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
