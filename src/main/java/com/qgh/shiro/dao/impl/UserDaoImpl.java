package com.qgh.shiro.dao.impl;

import com.qgh.shiro.bean.User;
import com.qgh.shiro.dao.UserDao;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 * Created with IDEA
 * author:秦光泓
 * description:
 * Date:2020/2/22
 * Time:15:57
 */
@Component//将它创建成对象
public class UserDaoImpl implements UserDao {
    //注入JdbcTemplate
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Override
    public User getUserByUserName(String userName) {
        //之前这个sql 写错了 报错了
        String sql="select username, password from users where username=?";
        List<User> list=jdbcTemplate.query(sql, new String[]{userName}, new RowMapper<User>() {
                    @Override
                    public User mapRow(ResultSet resultSet, int i) throws SQLException {
                        User user=new User();
                        //将查询到的结果集设置到对象中
                        user.setUsername(resultSet.getString("username"));
                        user.setPassword(resultSet.getString("password"));
                        return user;
                    }
                }
        );
        if ((CollectionUtils.isEmpty(list))){
            return  null;
        }
        return list.get(0);//一般用户名是不能重复的
    }

    /**
     * 根据用户名查询角色
     * @param userName
     * @return
     */
    @Override
    public List<String> queryRolesByUserName(String userName) {
         String   sql="select role_name from user_roles where username=?";

        return jdbcTemplate.query(sql, new String[]{userName}, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("role_name");
            }
        });
    }
}
