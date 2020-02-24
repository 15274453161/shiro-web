package com.qgh.shiro.controller;

import com.qgh.shiro.bean.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 秦光泓
 * @title:
 * @projectName shiro-web
 * @description: TODO
 * @date 2020/2/2115:47
 */
@Controller
public class UserController {
    @RequestMapping(value = "/subLogin", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String subLogin(User user) {
        //获得主体
        Subject subject = SecurityUtils.getSubject();
       //将前端传过来的密码变为字符数组
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());

        try {
            subject.login(token);//这儿会抛出异常
        } catch (AuthenticationException e) {
            return e.getMessage();
        }
       if(subject.hasRole("admin")){
       return "存在admin角色";
       }
        return "登录成功";

    }

    @RequestMapping("/test")

    public String test() {
        System.out.println("测试页面返回");
        return "main";
    }
   // @RequiresRoles("admin")//表示当前的主体必须具有admin角色才能访问这个链接 也可以在配置文件中配置spring.xml的过滤器中配置
    @RequestMapping(value="/testRole",method = RequestMethod.GET)
    @ResponseBody
    public String testRole(){
        return "testRole success";
    }

  //  @RequiresRoles("admin1")//表示当前的主体必须具有admin角色才能访问这个链接
    //@RequiresPermissions("xxx")//可以传入数组 可以传入多个参数 当前主体具备当前权限才能访问这个方法  推荐使用
    @RequestMapping(value="/testRole1",method = RequestMethod.GET)
    @ResponseBody
    public String testRole1(){
        return "testRole1 success";
    }

    @RequestMapping(value="/testPerms1",method = RequestMethod.GET)
    @ResponseBody
    public String testPerms1(){
        return "testPerms1 success";
    }
    @RequestMapping(value="/testPerms2",method = RequestMethod.GET)
    @ResponseBody
    public String testPerms2(){
        return "testPerms2 success";
    }
}
