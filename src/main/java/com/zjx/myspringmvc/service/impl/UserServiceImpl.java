package com.zjx.myspringmvc.service.impl;

import com.zjx.myspringmvc.annotaion.MyService;
import com.zjx.myspringmvc.service.UserService;

/**
 * Created by HP on 2019/7/5.
 */
@MyService("UserServiceImpl")
public class UserServiceImpl implements UserService {
    public String query(String name, String age) {
        return "name:"+name+";age:"+age;
    }

    public String insert(String param) {
        return "insert successfully!";
    }

    public String update(String param) {
        return "update successfully!";
    }
}
