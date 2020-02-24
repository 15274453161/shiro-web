package com.qgh.shiro.bean;

/**
 * @author 秦光泓
 * @title:对象用来接收认证信息
 * @projectName shiro-web
 * @description: TODO
 * @date 2020/2/2115:49
 */
public class User {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
