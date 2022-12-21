package dao;

import domain.User;

import java.util.List;

/**
 * @author yhw
 * @version 1.0
 **/
public class UserDao extends BasicDAO<User> {

    /**
     * 通过用户名查询
     *
     * @param username
     * @return
     */
    public User queryByUsername(String username) {
        String sql = "select * from t_user where `username` = ?";
        User user = queryOne(sql, User.class, username);
        return user;
    }

    /**
     * 通过手机号查询
     *
     * @param phoneNum
     * @return
     */
    public User queryByPhoneNum(String phoneNum) {
        String sql = "select * from t_user where `phoneNum` = ?";
        User user = queryOne(sql, User.class, phoneNum);
        return user;
    }

    /**
     * 通过用户名密码登录，如果存在返回对象
     *
     * @param username
     * @param password
     * @return
     */
    public User queryUserByUsernameAndPassword(String username, String password) {
        String sql = "select * from t_user where `username`=? and `password` = ?";
        User user = queryOne(sql, User.class, username, password);
        return user;
    }

    public int saveUser(User user) {
        String sql = "insert into t_user values(?,?,?)";
        return update(sql,user.getUsername(),user.getPassword(),user.getPhoneNum());
    }

    public User queryByUsernameAndPhoneNum(String username, String phoneNum) {
        String sql = "select * from t_user where `username`=? and `phoneNum` = ?";
        User user = queryOne(sql,User.class,username,phoneNum);
        return user;
    }
    public int resetPassword(String username,String password){
        String sql = "update t_user set password = ? where username=? ";
        return update(sql,password,username);
    }

    public Long queryTotalCount(String selectType, String keyboard) {
        String sql = "select count(*) from t_user where "+selectType+" like '%"+keyboard+"%'";
        System.out.println(sql);
        return (Long) queryScalar(sql);
    }

    public List<User> queryPage(int i, int pageSize, String selectType, String keyboard) {
        String sql = "select * from t_user where "+selectType+" like '%"+keyboard+"%' limit ?,?";
        System.out.println(sql);
        List<User> users = queryMulti(sql, User.class, i, pageSize);

        return users;
    }

    public int deleteByUsername(String username) {
        String sql = "delete from t_user where username = ?";
        return update(sql,username);
    }
}
