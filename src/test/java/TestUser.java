import dao.UserDao;
import domain.Page;
import domain.User;
import org.junit.Test;
import service.UserService;

/**
 * @author yhw
 * @version 1.0
 **/
public class TestUser {

    private UserService userService;
    private UserDao userDao;
    @Test
    public void test(){
        userService = new UserService();
        User user = new User();
        user.setUsername("dadadad");
        user.setPassword("daadadadad");
        System.out.println(userService.loginService(user));
    }

    @Test
    public void test2(){
        userDao = new UserDao();
        System.out.println(        userDao.queryTotalCount("username",""));
    }

    @Test
    public void test3(){
        userDao = new UserDao();
        System.out.println(userDao.queryPage(0,3,"username","167"));
    }

    @Test
    public void test4(){
        userService = new UserService();
        System.out.println(userService.page(1, Page.PAGE_SIZE,"username",""));
    }
}
