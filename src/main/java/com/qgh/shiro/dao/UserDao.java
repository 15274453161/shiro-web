package com.qgh.shiro.dao;

import com.qgh.shiro.bean.User;

import java.util.List;


/**
 * Created with IDEA
 * author:秦光泓
 * description:
 * Date:2020/2/22
 * Time:15:56
 */
public interface UserDao {
    User  getUserByUserName(String userName);
    List<String> queryRolesByUserName(String userName);
}
