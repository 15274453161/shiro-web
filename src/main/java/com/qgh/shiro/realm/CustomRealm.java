package com.qgh.shiro.realm;

import com.qgh.shiro.bean.User;
import com.qgh.shiro.dao.UserDao;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author 秦光泓
 * @title:自定义Realm 结合连接数据库测试
 * @projectName shiro-web
 * @description: TODO
 * @date 2020/2/2115:35
 */
public class CustomRealm extends AuthorizingRealm {
    //1、自动注入UserDao
    @Autowired
    private UserDao userDao;

    /* Map<String,String> userMap=new HashMap<>();
     {
         userMap.put("qgh","699bf22c576c5424ed79a9b7dafd2403");
         //设置realm的mingz
         super.setName("customRealm");
     }*/
    @Override//授权
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String userName = (String) principalCollection.getPrimaryPrincipal();
        //根据用户名获取用户角色  从数据库护照缓存中获取角色数据  模拟
        Set<String> roles = getRolesByUserName(userName);
        //通过用户名获取权限
        Set<String> permissions = getPermissionByUserName(userName);

        //取出的数据返回 设置权限和角色
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setStringPermissions(permissions);//这个地方之前返回 null 导致授权没有成功
        simpleAuthorizationInfo.setRoles(roles);
        return simpleAuthorizationInfo;
    }

    /***
     * 模拟数据库 或者缓存
     * @param userName
     * @return java.util.Set<java.lang.String>
     * @date 2020/2/21 11:13
     */

    public Set<String> getRolesByUserName(String userName) {
        Set<String> sets = new HashSet<>();
        sets.add("admin");
       // sets.add("user");
        return sets;
    }

    /***
     * 模拟数据库获得权限
     * @param userName
     * @return java.util.Set<java.lang.String>
     * @date 2020/2/21 11:15
     */

    public Set<String> getPermissionByUserName(String userName) {

        /*Set<String> sets = new HashSet<>();
        sets.add("user:delete");
        sets.add("user:update");
        return sets;*/
        List<String> list=userDao.queryRolesByUserName(userName);
        Set<String> set=new HashSet<>(list);
        return set;

    }

    /***
     * 认证
     * @param authenticationToken
     * @return org.apache.shiro.authc.AuthenticationInfo
     * @date 2020/2/21 17:00
     */

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //1、根据主题传过来的信息获取用户名
        String userName = (String) authenticationToken.getPrincipal();


        //2、通过用户名到数据库中查找密码
        String password = getPasswordByUserName(userName);

        if (password == null) {
            return null;
        }
        //存在用户 构造一个返回对象
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userName, password, getName());
        //密码加盐
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(userName));

        return authenticationInfo;
    }

    /**
     * 从数据中获取用户信息
     * @param userName
     * @return
     */

    public String getPasswordByUserName(String userName) {
        User user=userDao.getUserByUserName(userName);
        if(user!=null){
            return user.getPassword();
        }
        //return userMap.get("qgh");
        return null;
    }
   /* //密码加密
    public static void main(String[] args) {
        // 加密后的密文是 e10adc3949ba59abbe56e057f20f883e
        //密码加盐以后  283538989cef48f3d7d8a1c1bdf2008f
        //                 source=密码  salt=加盐值
        Md5Hash md5Hash=new Md5Hash("123456","Mark");//283538989cef48f3d7d8a1c1bdf2008f
        System.out.println(md5Hash.toString());
    }*/
}
