package com.zjx.myspringmvc.service;

/**
 * Created by HP on 2019/7/5.
 */
public interface UserService {
    String query(String name,String age);
    String insert(String param);
    String update(String param);
}
