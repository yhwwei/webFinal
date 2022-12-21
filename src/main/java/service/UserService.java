package service;

import dao.UserDao;
import domain.Page;
import domain.User;

/**
 * @author yhw
 * @version 1.0
 **/
public class UserService {
    private UserDao userDao = new UserDao();

    public int isExistUser(User user) {
        if (userDao.queryByUsername(user.getUsername()) != null) {
            return -1;
        }
        if (userDao.queryByPhoneNum(user.getPhoneNum()) != null) {
            return -2;
        }
        return 0;
    }

    /**
     * 账号密码登录
     *
     * @param user
     * @return
     */
    public User loginService(User user) {
        return userDao.queryUserByUsernameAndPassword(user.getUsername(), user.getPassword());
    }

    public User loginByPhoneService(String phoneNum) {
        return userDao.queryByPhoneNum(phoneNum);
    }

    public void registerService(User user) {
        userDao.saveUser(user);
    }

    public User queryByUsernameAndPhoneNum(User user) {
        return userDao.queryByUsernameAndPhoneNum(user.getUsername(), user.getPhoneNum());
    }
    public int resetPassword(User user){
        return userDao.resetPassword(user.getUsername(),user.getPassword());
    }

    public Page<User> page(int pageNo, int pageSize, String selectType, String keyboard) {
        Page<User> page = new Page<>();
        page.setPageNo(pageNo);
        page.setItemsTotal(userDao.queryTotalCount(selectType,keyboard));
        page.setPageTotal((int) Math.ceil(page.getItemsTotal()/(double)pageSize));
        //得判断传进来的 pageNo是不是在范围内
        page.setPageNo(pageNo);
        page.setObjs(userDao.queryPage((page.getPageNo()-1)*pageSize,pageSize,selectType,keyboard));
        return page;
    }

    public void deleteByUsername(String username) {
          userDao.deleteByUsername(username);
    }
}
